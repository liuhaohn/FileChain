import com.google.gson.Gson;
import com.hao.NodeState;
import org.assertj.core.api.Assertions;
import org.hyperledger.fabric.contract.execution.JSONTransactionSerializer;
import org.hyperledger.fabric.contract.metadata.TypeSchema;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class CompKeyTest {
    @Test
    public void test() {
        ChaincodeStub stub = Mockito.mock(ChaincodeStub.class);
        Mockito.when(stub.getMspId()).thenReturn("myMspId");
        CompositeKey a = new CompositeKey(NodeState.class.getSimpleName(), "a");
        System.out.println(a);
        Assertions.assertThat(a).isEqualToComparingFieldByField(null);
    }

    @Test
    public void testT() {
        List<NodeState> list = new ArrayList<>();
        list.add(new NodeState("adf","1","210.47.52.1","81"));
        list.add(new NodeState("adf","1","210.47.52.1","81"));
        list.add(new NodeState("adf","1","210.47.52.1","81"));
        list = null;
        System.out.println(new Gson().toJson(list));
    }
}


