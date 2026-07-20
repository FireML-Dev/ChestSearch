## ChestSearch

A Paper plugin that allows players to find items in nearby chests with a single command.

All blocks within range of the player are checked. This range defaults to 5 can be configured in config.yml.

### Commands
| Command               | Arguments | Permission            | Description                                                                                                                 |
| --------------------- | --------- | --------------------- | --------------------------------------------------------------------------------------------------------------------------- |
| `/chestsearch reload` | `None`    | `chestsearch.admin`   | Reloads the plugin.                                                                                                         |
| `/chestsearch hand`   | `None`    | `chestsearch.command` | Searches for the item in your main hand.                                                                                    |
| `/chestsearch name`   | `<name>`  | `chestsearch.command` | Searches for items matching the given name (case-insensitive). If no exact match is found, it will attempt a partial match. |
| `/chestsearch type`   | `<type>`  | `chestsearch.command` | Searches for items matching the given item type. (e.g. `minecraft:player_head`)                                             |
