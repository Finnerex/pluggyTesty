package co.tantleffbeef.pluggytesty.expeditions.listeners;

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
  public void onPlayerDamage(EntityDamageByEntityEvent event) {
    if(!(event.getDamager() instanceof Player damager))
      return;
    
    if (!(event.getEntity() instanceof Player damaged))
      return;
    
    Party party = partyManager.getPartyWith(damager);
    
    if (party.containsPlayer(damaged) && !party.getFriendlyFireEnabled())
      event.setCancelled(true);
    
  }

}
