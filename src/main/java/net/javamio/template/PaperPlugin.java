package net.javamio.template;

import lombok.Getter;
import net.javamio.template.command.impl.HelloCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;

import java.util.logging.Logger;

@Getter
public class PaperPlugin extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("PaperPlugin-Template");

    @Getter
    private static PaperPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        PaperCommandManager<Source> commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this);
        HelloCommand.register(commandManager);

        LOGGER.info("PaperPlugin-Template has been successfully enabled");
    }

}
