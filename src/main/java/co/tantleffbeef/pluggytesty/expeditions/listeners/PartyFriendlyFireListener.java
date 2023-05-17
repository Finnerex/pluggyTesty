package co.tantleffbeef.pluggytesty.expeditions.listeners;

import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import co.tantleffbeef.pluggytesty.expeditions.*;

public class PartyFriendlyFireListener implements Listener {

  private final PartyManager partyManager;

  public PartyFriendlyFireListener(PartyManager partyManager) {
    this.partyManager = partyManager;
  }
  
  @EventHandler
  public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player damaged))
      return;
    
    Player damager;
    if(event.getDamager() instanceof Player) {
      damager = (Player) event.getDamager();
      
    } else {
      
      if (!(event.getDamager() instanceof Projectile projectile))
        return;
      if (!(projectile.getShooter() instanceof Player))
        return;
      
      damager = (Player) projectile.getShooter();
    }
    
    Party party = partyManager.getPartyWith(damager);
    
    if (party.containsPlayer(damaged) && !party.getFriendlyFireEnabled())
      event.setCancelled(true);
    
  }

//   @EventHandler
//   public void onPlayerDamageByProjectile(EntityDamageByEntityEvent event) {
//     if(!(event.getDamager() instanceof Projectile projectile))
//       return;

//     if (!(event.getEntity() instanceof Player damaged))
//       return;

//     if (!(projectile.getShooter() instanceof Player damager))
//       return;

//     Party party = partyManager.getPartyWith(damager);

//     if (party.containsPlayer(damaged) && !party.getFriendlyFireEnabled())
//       event.setCancelled(true);

//   }

}
