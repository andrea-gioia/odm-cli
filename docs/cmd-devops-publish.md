# odmcli devops publish

## Usage

`odmcli devops publish`

## Description

Publish an entity in the `devops-service`
   
## Options

Option|Default|Description
-------|----------|-------
`--entity-file`|| File that contains the entity description
`--type`||Type of the entity to publish (`activity`\|`task`)

## Examples

### Publish a task
```bash
./odmcli devops publish --type task --entity-file task.json
```



