/*
 * This file is part of spark.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucko.spark.nemisys;

import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.SparkPlugin;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import org.itxtech.nemisys.command.Command;
import org.itxtech.nemisys.command.CommandSender;
import org.itxtech.nemisys.plugin.PluginBase;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.stream.Stream;

public class NemisysSparkPlugin extends PluginBase implements SparkPlugin {
    private SparkPlatform platform;

    @Override
    public void onEnable() {
        this.platform = new SparkPlatform(this);
        this.platform.enable();
    }

    @Override
    public void onDisable() {
        this.platform.disable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.platform.executeCommand(new NemisysCommandSender(sender), args);
        return true;
    }

    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public Path getPluginDirectory() {
        return getDataFolder().toPath();
    }

    @Override
    public String getCommandName() {
        return "spark";
    }

    @Override
    public Stream<NemisysCommandSender> getCommandSenders() {
/*      // we currently only have ConsoleCommandSender
        return Stream.concat(
                getServer().getOnlinePlayers().values().stream(),
                Stream.of(getServer().getConsoleSender())
        ).map(NemisysCommandSender::new);
*/
        return Stream.of(new NemisysCommandSender(getServer().getConsoleSender()));
    }

    @Override
    public void executeAsync(Runnable task) {
        getServer().getScheduler().scheduleTask(task, true);
    }

    @Override
    public void log(Level level, String msg) {
        if (level.intValue() >= 1000) { // severe
            getLogger().error(msg);
        } else if (level.intValue() >= 900) { // warning
            getLogger().warning(msg);
        } else {
            getLogger().info(msg);
        }
    }

    @Override
    public void log(Level level, String msg, Throwable throwable) {
        if (level.intValue() >= 1000) { // severe
            getLogger().error(msg, throwable);
        } else if (level.intValue() >= 900) { // warning
            getLogger().warning(msg, throwable);
        } else {
            getLogger().info(msg, throwable);
        }
    }

    @Override
    public ClassSourceLookup createClassSourceLookup() {
        return new NemisysClassSourceLookup();
    }

    @Override
    public PlayerPingProvider createPlayerPingProvider() {
        return new NemisysPlayerPingProvider(getServer());
    }

    @Override
    public PlatformInfo getPlatformInfo() {
        return new NemisysPlatformInfo(getServer());
    }
}
