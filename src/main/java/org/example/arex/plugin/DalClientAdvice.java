package org.example.arex.plugin;

import com.your.company.dal.DalClient;
import io.arex.agent.bootstrap.model.MockResult;
import io.arex.agent.bootstrap.model.Mocker;
import io.arex.inst.runtime.context.ContextManager;
import io.arex.inst.runtime.serializer.Serializer;
import io.arex.inst.runtime.util.MockUtils;

public class DalClientAdvice {
    /**
     * 录制
     */
    public static void record(DalClient.Action action, String param, Object result) {
        if (ContextManager.needRecord()) {
            Mocker mocker = buildMocker(action, param);
            mocker.getTargetResponse().setBody(Serializer.serialize(result));
            MockUtils.recordMocker(mocker);
        }
    }

    /**
     * 回放
     */
    public static MockResult replay(DalClient.Action action, String param) {
        if (ContextManager.needReplay()) {
            Mocker mocker = buildMocker(action, param);
            Object result = MockUtils.replayBody(mocker);
            return MockResult.success(result);
        }
        return null;
    }

    private static Mocker buildMocker(DalClient.Action action, String param) {
        Mocker mocker = MockUtils.createDatabase(action.name().toLowerCase());
        mocker.getTargetRequest().setBody(param);
        return mocker;
    }
}
