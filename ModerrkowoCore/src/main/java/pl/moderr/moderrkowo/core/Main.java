package pl.moderr.moderrkowo.core;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import pl.moderr.moderrkowo.core.commands.admin.*;
import pl.moderr.moderrkowo.core.commands.player.DiscordCommand;
import pl.moderr.moderrkowo.core.commands.player.RegulaminCommand;
import pl.moderr.moderrkowo.core.commands.player.messages.HelpopCommand;
import pl.moderr.moderrkowo.core.commands.player.messages.MessageCommand;
import pl.moderr.moderrkowo.core.commands.player.messages.ReplyCommand;
import pl.moderr.moderrkowo.core.commands.player.pogoda.PogodaCommand;
import pl.moderr.moderrkowo.core.commands.player.rynek.RynekCommand;
import pl.moderr.moderrkowo.core.commands.player.rynek.RynekManager;
import pl.moderr.moderrkowo.core.commands.player.teleport.*;
import pl.moderr.moderrkowo.core.cuboids.ModerrCuboids;
import pl.moderr.moderrkowo.core.listeners.*;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;
import pl.moderr.moderrkowo.core.utils.Logger;
import pl.moderr.moderrkowo.database.ModerrkowoDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public final class Main extends JavaPlugin {

    public DatabaseListener dbListener = new DatabaseListener();
    private static Main instance;
    public ModerrAntyLogout antyLogout;
    public RynekManager rynekManager;

    public static String getServerName() {
        return ColorUtils.color(HexResolver.parseHexString(Main.getInstance().getConfig().getString("servername")));
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Logger.logAdminLog("Wczytywanie pluginu... [CORE]");
        // Constructor
        instance = this;
        // Listeners
        try{
            ModerrkowoDatabase.getInstance().registerDatabaseListener(dbListener);
        }catch(Exception ignored){

        }
        Bukkit.getPluginManager().registerEvents(new PortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new MotdListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new MendingRemover(), this);
        antyLogout = new ModerrAntyLogout();
        Bukkit.getPluginManager().registerEvents(antyLogout, this);
        Logger.logAdminChat("Wczytano listenery");
        // Commands | admin
        Objects.requireNonNull(getCommand("alogi")).setExecutor(new ALOGI_Command());
        Objects.requireNonNull(getCommand("ahelpop")).setExecutor(new AHelpopCommand());
        Objects.requireNonNull(getCommand("adminchat")).setExecutor(new AdminChatCommand());
        Objects.requireNonNull(getCommand("ainvsee")).setExecutor(new InvseeCommand());
        Objects.requireNonNull(getCommand("asendalert")).setExecutor(new SendAlertCommand());
        Objects.requireNonNull(getCommand("aclearchat")).setExecutor(new ClearChatCommand());
        // Commands | teleport
        Objects.requireNonNull(getCommand("tpa")).setExecutor(new TPACommand());
        Objects.requireNonNull(getCommand("tpdeny")).setExecutor(new TPDeny());
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new TPAccept());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());
        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        // Commands | pogoda
        Objects.requireNonNull(getCommand("pogoda")).setExecutor(new PogodaCommand());
        // Commands | messages
        Objects.requireNonNull(getCommand("helpop")).setExecutor(new HelpopCommand());
        Objects.requireNonNull(getCommand("msg")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("reply")).setExecutor(new ReplyCommand());
        // Commands | rynek
        rynekManager = new RynekManager();
        Objects.requireNonNull(getCommand("rynek")).setExecutor(new RynekCommand());
        // Commands | end
        Objects.requireNonNull(getCommand("say")).setExecutor(new SayCommand());
        Objects.requireNonNull(getCommand("regulamin")).setExecutor(new RegulaminCommand());
        Objects.requireNonNull(getCommand("discord")).setExecutor(new DiscordCommand());
        Logger.logAdminChat("Wczytano komendy");

        new ModerrCuboids().start();

        ArrayList<String> message = new ArrayList<>();
        message.add("&eJeżeli chcesz aby zmienić pogodę na ładną użyj &c/pogoda!");
        message.add("&eTwój przyjaciel jest daleko? Użyjcie &c/tpa");
        message.add("&eWidzisz buga? Prosimy zgłoś go na &c/helpop");
        //message.add("&eWkurza Cię Scoreboard? Wyłącz go! &c/sidebar");
        message.add("&eJeżeli chcesz napisać do kogoś prywatną wiadomość użyj &c/msg!");
        message.add("&eAby szybko komuś odpisać na prywatną wiadomośc użyj &c/r");
        message.add("&eTwoję miejsce śmierci zawsze będzie napisane na chacie!");
        message.add("&ePotrzebujesz działki? &cMusisz ją scraftować!");
        message.add("&ePortal endu jest wyłączony!");
        message.add("&ePo uderzeniu moba/gracza wyświetla się ilośc zadanych serc");
        message.add("&eNa naszym serwerze możesz sobie ustawić dom! &c/ustawdom");
        message.add("&eAby przeteleportować się do domu użyj &c/dom");
        //message.add("&eChcesz kupować rzadkie przedmioty? &c/kupno");
        //message.add("&eAby zdobywać punkty musisz zabijać graczy");
        //message.add("&eNie jesteś zapozany z regulaminem? &c/regulamin");
        message.add("&eGdy jesteś podczas walki nie możesz się wylogować!");
        message.add("&eGodziny walki trwają od &c18-8");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            String messageS = message.get(new Random().nextInt(message.size()));
            Bukkit.broadcastMessage(ColorUtils.color("&8[&b❄&8] " + messageS));
        }, 0, 20 * 60 * 2);
        Logger.logAdminChat("Wczytano AUTOMSG");

        Logger.logAdminLog("Wczytano plugin [CORE] w &8(&a" + (System.currentTimeMillis() - start) + "ms&8)");
    }

    @Override
    public void onDisable() {
        rynekManager.Save();
    }

    public static Main getInstance(){
        return instance;
    }
    public int getBorder(World world) {
        return (int) world.getWorldBorder().getSize();
    }
}
