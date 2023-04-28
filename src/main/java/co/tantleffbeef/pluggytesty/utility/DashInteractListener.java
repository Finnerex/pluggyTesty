/* public class DashInteractListener imlpements Listener {
    private Plugin plugin;

    public DashInteractListener(Plugin plugin) {
        this.plugin = plugin
    }
     private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.FEATHER)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(HealingHeart.DASH_LORE))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.FEATHER))
            return;
        player.playSound(player, Sound.ENTITY_EGG_THROW, 1, 1);

        player.setCooldown(Material.FEATHER, 80);

        plugin.getServer().getScheduler().runTask(plugin, () -> Dash(player));

       private void Dash(Player player) {
        Location location = player.getEyeLocation();

        player.setVelocity(location.getDirection().normalize());
       }
    }
}