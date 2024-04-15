package org.example.arex.plugin;

import com.your.company.dal.DalClient;
import io.arex.agent.bootstrap.model.MockResult;
import io.arex.inst.extension.MethodInstrumentation;
import io.arex.inst.extension.TypeInstrumentation;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;

import static java.util.Collections.singletonList;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArgument;

public class DalClientInstrumentation extends TypeInstrumentation {
    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        // 这里声明的是你要修饰的类的全限定名，不是io.arex*开头的类
        return named("com.your.company.dal.DalClient");
    }

    @Override
    public List<MethodInstrumentation> methodAdvices() {
        ElementMatcher<MethodDescription> matcher = named("invoke")
                .and(takesArgument(0, named("com.your.company.dal.DalClient$Action")))
                .and(takesArgument(1, named("java.lang.String")));

        return singletonList(new MethodInstrumentation(matcher, InvokeAdvice.class.getName()));
    }

    public static class InvokeAdvice {
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class, suppress = Throwable.class)
        public static boolean onEnter(@Advice.Argument(0) DalClient.Action action,
                                      @Advice.Argument(1) String param,
                                      @Advice.Local("mockResult") MockResult mockResult) {
            mockResult = DalClientAdvice.replay(action, param); // 回放
            return mockResult != null && mockResult.notIgnoreMockResult();
        }

        @Advice.OnMethodExit(suppress = Throwable.class)
        public static void onExit(@Advice.Argument(0) DalClient.Action action,
                                  @Advice.Argument(1) String param,
                                  @Advice.Local("mockResult") MockResult mockResult,
                                  @Advice.Return(readOnly = false) Object result) {
            if (mockResult != null && mockResult.notIgnoreMockResult()) {
                result = mockResult.getResult(); // 使用回放结果
                return;
            }
            DalClientAdvice.record(action, param, result); // 录制
        }
    }
}
