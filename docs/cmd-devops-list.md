# odmcli devops list

## Usage

`odmcli devops list`

## Description

List `devops-service` entities (i.e. `activity` or `task`)

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
