package cn.minezone.myspawners.handlers;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand extends Cloneable {

    /**
     * 执行此命令
     * @param commandSender 发送者
     * @param args 参数（只有你自己的参数，没有前置）
     * @return 是否成功执行（false：提示帮助信息，true：跳过）
     * @throws CommandException 处理器会getMessage()并反馈给用户
     */
    boolean execute(CommandSender commandSender, String[] args) throws CommandException;//执行

    List<String> onTabComplete(CommandSender commandSender, String[] args);//Tab完成

    boolean hasPermission(CommandSender sender);//是否有权限

    String getPermission();//获取权限节点

    int getMinimumArguments();//获取最小参数数量

    int getMaximumArguments();//获取最大参数数量

    String getName();//获取命令

    String getHelp();//获取一句话帮助

    List<String> getHelps();//获取完整帮助

    SubCommand clone();//克隆对象，深拷贝
}