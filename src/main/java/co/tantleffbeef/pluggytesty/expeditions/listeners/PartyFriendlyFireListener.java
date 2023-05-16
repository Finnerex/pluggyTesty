package co.tantleffbeef.pluggytesty.expeditions.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import co.tantleffbeef.pluggytesty.expeditions.PartyManager;

public class PartyFriendlyFireListener implements Listener {

  private final PartyManager partyManager;

  public PartyFriendlyFireListener(PartyManager partyManager) {
    this.partyManager = partyManager;
  }
  
  @EventHandler
  public void onPlayerDamage(EntityDamageByEntityEvent event) {
    if(!(event.getDamager() instanceof Player damager))
      return;
    
    if (!(event.getEntity() instanceof Player damaged))
      return;
    
    if (partyManager.getPartyWith(damager).containsPlayer(damaged))
      event.setCancelled(true);
    
  }

}
