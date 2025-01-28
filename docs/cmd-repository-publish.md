# odmcli repository publish

## Usage

`odmcli repository publish`

## Description

Publish an entity in the `repository-service`

**⚒️Implementation details:**

1. TODO
   
## Options

Option|Default|Description
-------|----------|-------
`-f, --file`|| File that contains the definition of the entity to publish
`--type`||Type of the entity to get (dp\|dpv)

## Examples

### Publish a local descriptor in the repository
```bash
./odmcli registry publish --type dpv -f dpd/data-product-descriptor.json
```



