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

### List data products
```bash
./odmcli devops list --type activity
```

### List data product versions
```bash
./odmcli devops list --type task 
```
