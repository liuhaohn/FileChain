package com.hao;

import com.google.gson.JsonArray;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Contract(name = "fncc")
@Default
public class FileNodeContract implements ContractInterface {

    private final static Logger LOG = Logger.getLogger(com.hao.FileNodeContract.class.getName());
    private Pattern pattern = Pattern.compile("O=(.*)");

    @Transaction
    public void instantiate(Context ctx) {
        ctx.getStub().putStringState("count", "0");
        LOG.info("File Contract is instantiated");
    }

    private String parseOrganization(X509Certificate x509Certificate) {
        String name = x509Certificate.getSubjectX500Principal().getName();
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return matcher.group(1).split(",")[0];
        }
        return "";
    }

    /**
     * File 的组合键是：FileState Hash
     */
    @Transaction
    public FileState insertFile(Context ctx, String json) {
        ChaincodeStub stub = ctx.getStub();
        String clientMspId = parseOrganization(ctx.getClientIdentity().getX509Certificate());
        FileState fileState = FileState.fromJson(json, FileState.class);
        // 插入文件的msp必须和客户端msp相同
        if (!clientMspId.equals(fileState.getOrganization())) {
            LOG.warning(String.format("%s attempted to insert a file, but the file's owner is %s", clientMspId, fileState.getOrganization()));
            return null;
        }

        CompositeKey compositeKey = new CompositeKey(FileState.class.getSimpleName(), fileState.getFileHash());
        byte[] state = stub.getState(compositeKey.toString());
        FileState localFileState = FileState.deserialize(state, FileState.class);

        if (localFileState != null) {
            if (localFileState.isDeleted()) {
                localFileState.setState(FileState.STATE_EXIST);
                fileState = localFileState;
                state = localFileState.serialize();
                stub.putState(compositeKey.toString(), state); // update
                LOG.info("insert a deleted file, update its state");
            } else {
                if (fileState.getFileSize() != localFileState.getFileSize()) {
                    LOG.warning("insert error, hash conflict");
                    return null;
                }
            }
        } else {
            state = fileState.serialize();
            stub.putState(compositeKey.toString(), state);
            LOG.info("insert a new file: " + fileState.toString());
        }
        stub.setEvent("InsertFileEvent", state);

        // 更新node的负载和负债
        String key = new CompositeKey(NodeState.class.getSimpleName(), clientMspId).toString();
        byte[] organizationB = stub.getState(key);
        NodeState organization = NodeState.deserialize(organizationB, NodeState.class);
        long add = (long) (fileState.getFileSize() * 1.5);
        organization.setDebt(organization.getDebt() + add);  // 增加负债
        LOG.info("FileNode debt add: " + organization + " Add debt " + add);
        add = (long) (fileState.getFileSize() * 0.5);
        organization.setLoad(organization.getLoad() + add);  // 增加负载
        LOG.info("FileNode load add: " + organization + ". Add load " + add);
        organizationB = organization.serialize(); // 一次transaction对一个可以只能修改一次（读写集特性），所以clientMsp必须在负债和负载一起修改
        stub.putState(key, organizationB);

        List<String> sliceOrganization = fileState.getSliceOrganization();
        for (String mspId : sliceOrganization) {
            if (mspId.equals(clientMspId)) continue;
            key = new CompositeKey(NodeState.class.getSimpleName(), mspId).toString();
            organizationB = stub.getState(key);
            organization = NodeState.deserialize(organizationB, NodeState.class);
            organization.setLoad(organization.getLoad() + add);  // 增加负载
            organizationB = organization.serialize();
            LOG.info("FileNode load add: " + organization + ". Add load " + add);
            stub.putState(key, organizationB);
        }

        return fileState;
    }

    @Transaction
    public FileState selectFile(Context ctx, String fileHash) {
        ChaincodeStub stub = ctx.getStub();
        byte[] state = stub.getState(new CompositeKey(FileState.class.getSimpleName(), fileHash).toString());
        FileState file = FileState.deserialize(state, FileState.class);
        if (file != null && file.isExist()) {
            LOG.info("select a exist file: " + file.toString());
            return file;
        }
        return null;
    }

    @Transaction
    public FileState selectFileContainDeleted(Context ctx, String fileHash) {
        ChaincodeStub stub = ctx.getStub();
        byte[] state = stub.getState(new CompositeKey(FileState.class.getSimpleName(), fileHash).toString());
        FileState file = FileState.deserialize(state, FileState.class);
        if (file != null) {
            LOG.info("select a file: " + file.toString());
            return file;
        }
        return null;
    }

    @Transaction
    public FileState addAuthorizedOrganization(Context ctx, String fileHash, String mspId) {
        ChaincodeStub stub = ctx.getStub();

        String clientMspId = parseOrganization(ctx.getClientIdentity().getX509Certificate());
        String key = new CompositeKey(FileState.class.getSimpleName(), fileHash).toString();
        byte[] state = stub.getState(key);
        FileState fileState = FileState.deserialize(state, FileState.class);

        if (fileState != null && fileState.isExist()) {
            // 只有文件所有者可以添加访问权限
            if (!clientMspId.equals(fileState.getOrganization())) {
                LOG.warning(String.format("%s attempted to modify the file %s, but it is not the owner of the file", mspId, fileHash));
                return null;
            }
            List<String> authorizedOrganization = fileState.getAuthorizedOrganization();
            if (authorizedOrganization == null) authorizedOrganization = new ArrayList<>();
            authorizedOrganization.add(mspId);
            fileState.setAuthorizedOrganization(authorizedOrganization);
            state = fileState.serialize();
            stub.putState(key, state);

            LOG.info("File update: " + fileState);
            stub.setEvent("UpdateFileEvent", state);
            return fileState;
        }
        return null;
    }

    /**
     * 文件使用日志
     */
    @Transaction
    public FileState addUserOrganization(Context ctx, String fileHash, String mspId) {
        ChaincodeStub stub = ctx.getStub();

        String clientMspId = parseOrganization(ctx.getClientIdentity().getX509Certificate());
        String key = new CompositeKey(FileState.class.getSimpleName(), fileHash).toString();
        byte[] state = stub.getState(key);
        FileState fileState = FileState.deserialize(state, FileState.class);

        if (fileState != null && fileState.isExist()) {
            // 只有文件有权访问文件者可以添加文件使用日志
            List<String> authorizedOrganization = fileState.getAuthorizedOrganization();
            if (!clientMspId.equals(fileState.getOrganization()) && (authorizedOrganization == null || !authorizedOrganization.contains(clientMspId))) {
                LOG.info(String.format("%s attempted to modify the file %s, but it is not the authorized user of the file", mspId, fileHash));
                return null;
            }
            List<String> userOrganization = fileState.getUserOrganization();
            if (userOrganization == null) userOrganization = new ArrayList<>();
            userOrganization.add(mspId);
            fileState.setAuthorizedOrganization(userOrganization);
            state = fileState.serialize();
            stub.putState(key, state);

            LOG.info("File update: " + fileState);
            stub.setEvent("UpdateFileEvent", state);
            return fileState;
        }
        return null;
    }

    @Transaction
    public FileState deleteFile(Context ctx, String fileHash) {
        ChaincodeStub stub = ctx.getStub();
        String clientMspId = parseOrganization(ctx.getClientIdentity().getX509Certificate());

        String key = new CompositeKey(FileState.class.getSimpleName(), fileHash).toString();
        byte[] state = stub.getState(key);
        FileState localFileState = FileState.deserialize(state, FileState.class);

        if (localFileState != null && localFileState.isExist()) {
            // 只有文件所有者可以删除文件
            if (!clientMspId.equals(localFileState.getOrganization())) {
                LOG.warning(String.format("%s attempted to delete the file %s, but it is not the owner of the file", clientMspId, localFileState.getFileHash()));
                return null;
            }

            localFileState.setState(FileState.STATE_DELETED);
            state = localFileState.serialize();
            stub.putState(key, state);
            stub.setEvent("DeleteFileEvent", state);
            LOG.info("delete a file: " + localFileState.toString());

            // 更新node的负载和负债
            key = new CompositeKey(NodeState.class.getSimpleName(), localFileState.getOrganization()).toString();
            byte[] organizationB = stub.getState(key);
            NodeState organization = NodeState.deserialize(organizationB, NodeState.class);
            long reduce = (long) (localFileState.getFileSize() * 1.5);
            organization.setDebt(organization.getDebt() - reduce);  // 减少负债
            LOG.info("FileNode debt reduce: " + organization + ". Reduce debt " + reduce);
            reduce = (long) (localFileState.getFileSize() * 0.5);
            organization.setLoad(organization.getLoad() - reduce);  // 减少负载
            LOG.info("FileNode load reduce: " + organization + ". Reduce load " + reduce);
            organizationB = organization.serialize();
            stub.putState(key, organizationB);

            for (String mspId : localFileState.getSliceOrganization()) {
                if (mspId.equals(clientMspId)) continue;
                key = new CompositeKey(NodeState.class.getSimpleName(), mspId).toString();
                organizationB = stub.getState(key);
                organization = NodeState.deserialize(organizationB, NodeState.class);
                organization.setLoad(organization.getLoad() - reduce);  // 减少负载
                organizationB = organization.serialize();
                LOG.info("FileNode load reduce: " + organization + ". Reduce load " + reduce);
                stub.putState(key, organizationB);
            }

            return localFileState;
        }

        return null;
    }


    @Transaction
    public NodeState insertNode(Context ctx, String json) {
        ChaincodeStub stub = ctx.getStub();
        String mspId = parseOrganization(ctx.getClientIdentity().getX509Certificate());
        NodeState nodeState = NodeState.fromJson(json, NodeState.class);
        // 插入的节点mspId需要和client的mspId相同
        if (!mspId.equals(nodeState.getMspId())) {
            LOG.warning(String.format("%s attempted to insert a file node, but the file node mspId is %s", mspId, nodeState.getMspId()));
            return null;
        }

        String count = new String(stub.getState("count"));

        byte[] state = stub.getState(new CompositeKey(NodeState.class.getSimpleName(), mspId).toString());
        NodeState localNodeState = NodeState.deserialize(state, NodeState.class);

        if (localNodeState != null) {
            LOG.info("insert node: " + localNodeState.toString() + " is already existed, it will be update to:" + nodeState);
        }

        nodeState.setIndex(count);
        stub.putStringState("count", String.format("%d", Integer.parseInt(count) + 1));

        byte[] serialize = nodeState.serialize();
        stub.putState(new CompositeKey(NodeState.class.getSimpleName(), mspId).toString(), serialize);
        LOG.info("Insert a new node: " + nodeState.toString());

        // 通知监听者有节点加入
        stub.setEvent("InsertNodeEvent", serialize);

        return nodeState;
    }

    @Transaction
    public String selectAllNode(Context ctx) {
        QueryResultsIterator<KeyValue> states = ctx.getStub()
                .getStateByPartialCompositeKey(new CompositeKey(NodeState.class.getSimpleName()));
        LOG.info("select all node");

        JsonArray array = new JsonArray();
        for (KeyValue keyValue : states) {
            byte[] value = keyValue.getValue();
            LOG.info("select all find file: " + NodeState.deserialize(value, NodeState.class));
            array.add(new String(value, StandardCharsets.UTF_8));
        }

        return array.toString();
    }

    @Transaction
    public String selectAllFile(Context ctx) {
        QueryResultsIterator<KeyValue> states = ctx.getStub()
                .getStateByPartialCompositeKey(new CompositeKey(FileState.class.getSimpleName()));
        LOG.info("select all file");

        JsonArray array = new JsonArray();
        for (KeyValue keyValue : states) {
            byte[] value = keyValue.getValue();
            FileState fileState = FileState.deserialize(value, FileState.class);
            LOG.info("select all find file: " + fileState);
            if (fileState.isExist()) {
                array.add(new String(value, StandardCharsets.UTF_8));
            }
        }

        return array.toString();
    }
}
