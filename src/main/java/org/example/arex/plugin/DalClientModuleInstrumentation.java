package org.example.arex.plugin;

import com.google.auto.service.AutoService;
import io.arex.inst.extension.ModuleInstrumentation;
import io.arex.inst.extension.TypeInstrumentation;

import java.util.List;

import static java.util.Collections.singletonList;

@AutoService(ModuleInstrumentation.class)
public class DalClientModuleInstrumentation extends ModuleInstrumentation {
    public DalClientModuleInstrumentation() {
        // 插件模块名，如果你的DalClient组件不同的版本之间代码差异比较大，且要分版本支持的话，可以按照框架的版本号匹配：
        // super("plugin-dal", ModuleDescription.builder().name("dalclient")
        // .supportFrom(ComparableVersion.of("1.0")).supportTo(ComparableVersion.of("2.0")).build());
        super("plugin-dal");
    }

    @Override
    public List<TypeInstrumentation> instrumentationTypes() {
        return singletonList(new DalClientInstrumentation());
    }
}
