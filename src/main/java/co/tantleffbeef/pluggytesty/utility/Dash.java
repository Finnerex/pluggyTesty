/* import java.util.ArrayList;

public class Dash implements CommandExecutor {

     public static final String DASH_LORE = "Right-Click to Dash";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(commandSender instanceof Player player))
            return false;

        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Dash");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(DASH_LORE);

        meta.setLore(lore);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
        player.updateInventory();

        return true;
    }
}