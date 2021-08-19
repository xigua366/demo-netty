package com.juejin.im.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 控制台命令抽象
 */
public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}