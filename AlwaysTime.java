//This is used for teaching people aswell as being a generic plugin on BukkitPlugins.
//Sorry for the excess amount of annotation if you are from BukkitPlugins.

package alwaystime;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class AlwaysTime extends JavaPlugin{

    public static int Day; //Used for cancelling on the night command
    public static int Night; //Used for cancelling on the day command
    
    @Override
    public void onEnable(){
        Bukkit.getLogger().info("AlwaysTimeEnabled!");
        Bukkit.getPluginManager().addPermission(new Permission("AlwaysTime.Change")); //Registers the permissions that
        // Will be used for checking if the player has the certain permission.
    }
    
    @Override
    public void onDisable(){
        Bukkit.getPluginManager().removePermission("AlwaysTime.Change");
        //Just for server efficiency its taken out and will be re-added on server restart/reload
    }
    @Override
    public boolean onCommand(CommandSender sender, Command  cmd,
            String commandLabel, String[] arg) {
        Player p = (Player) sender; //Casting the sender as a player, as the console can sometimes send commands.
        if (commandLabel.equalsIgnoreCase("alwaysday")){
            if (p.hasPermission("AlwaysTime.Change")){//Checks for the earlier permission, the server admin must have
            //Put the person to have permission otherwise this will not work (I think OP Overrides perms anyway)
            for (World world : Bukkit.getServer().getWorlds()){//This retrieves all worlds and sets them as World decleration
            //With the variable world, allowing world variable to be passed to the Public Void Day() down below.
                Day(world);
            }
         }else{
                p.sendMessage("Insufficient Permissions!"); //If player doesnt have AlwaysTime.Change perm, reverts.
                Bukkit.getLogger().info(p.getName() + "Tried to use always night command!"); //Logs the incident for admin usage
                Bukkit.getLogger().info("If this is a problem and you are op, make sure the perm 'AlwaysTime.Change' is in your perms config!");
            }
        }else if (commandLabel.equalsIgnoreCase("alwaysnight")){ //Repeat of above just with day time
            if (p.hasPermission("AlwaysTime.Change")){
              for (World world : Bukkit.getServer().getWorlds()){
                Night(world);
            }  
            }else{
                p.sendMessage("Insufficient Permissions!");
                Bukkit.getLogger().info(p.getName() + "Tried to use always night command!");
            }            
        }else if (commandLabel.equalsIgnoreCase("alwaystimecancel")){
            Bukkit.getScheduler().cancelTask(Day);
            Bukkit.getScheduler().cancelTask(Night);
        }
        return false; //Error - if a / With a space is casted, it just does nothing.
    }
    
    public void Day(final World w){ //Final means it can be access later in te class, as it is accessed by the repeatingtask
    //A World type MUST be passed in order for someone to do Day(), it cant be string. The worldi s then named w in this function
        Bukkit.getScheduler().cancelTask(Night); //Cancels it being locked into day, if its running.
        Bukkit.broadcastMessage("Locked into Day Time!"); //Broadcasts simple message.
        Day = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){     //Schedules new runnable with the reference to "THIS"      
            @Override
            public void run(){
                w.setTime(0);
            }
        
        }, 0, 100); //0 Means 0 tick delay before doing task. 100 Means 100 Ticks before repeating the set time again, which is 4 seconds in human time
        //As 20 ticks = 1 second
    }
    public void Night(final World w){ //Similar to the Day Function just as Night.
        Bukkit.getScheduler().cancelTask(Day);
        Bukkit.broadcastMessage("Locked into Night Time!");
        Night = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){            
            @Override
            public void run(){
                w.setTime(18000);
            } 
        }, 0, 100);
    }    
}
