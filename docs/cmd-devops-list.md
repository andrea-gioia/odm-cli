# odmcli devops list

## Usage

`odmcli devops list`

## Description

List `devops-service` entities (i.e. `ativity` or `task`)

**⚒️Implementation details:**

1. TODO

## Options

Option|Default|Description
-------|----------|-------
`--type`|`activity`|Type of the entity to list (`activity`\|`task`)

## Examples

### List activities
```bash
./odmcli devops list --type activity
```

### List tasks
```bash
./odmcli devops list --type task 
```
