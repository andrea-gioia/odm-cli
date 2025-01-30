# odmcli registry publish

## Usage

`odmcli registry publish`

## Description

Publish an entity in the `registry-service`
   
## Options

Option|Default|Description
-------|----------|-------
`--entity-file`|| File that contains the entity description
`--type`||Type of the entity to publish (`dp`\|`dpv`)
`--id`||Id of data product. Applicable only if `type` is equal to `dpv`

## Examples

### Publish a descriptor
```bash
./odmcli registry publish --type dpv --id dp123 --entity-file dpd/data-product-descriptor.json
```



