package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javax.security.auth.login.LoginException;

import commands.Balance;
import commands.Coinflip;
import commands.RockPaperScissors;
import database.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MainClass {

    private static final String TOKEN_FILE = "C:\\Users\\almog\\Desktop\\token.txt";

    public static Emote melaphecoin = null;
    public static String coin = null;

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
	String token = Files.readAllLines(new File(TOKEN_FILE).toPath(), Charset.defaultCharset()).get(0);

	JDA jda = JDABuilder.create(token, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).build();
	jda.addEventListener(new Coinflip());
	jda.addEventListener(new Balance());
	jda.addEventListener(new RockPaperScissors());
	Database.database(); // boot the database

	jda.awaitReady();
	melaphecoin = jda.getGuildById("699728425941991566").getEmotesByName("Melaphecoin", false).get(0);
	coin = " " + melaphecoin.getAsMention();
    }
}
