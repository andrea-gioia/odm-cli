# odmcli local init

## Usage

`odmcli local init`

## Description

Import from an external source (ex. jdbc) to a descriptor target element  (ex. output-port)

## Options

Option|Default|Description
-------|----------|-------
`-f, --file`|PATH/data-product-descriptor.json|Name of the descriptor file
`--from`| |Source
`--to`| |Target
`--in-param <KEY=VALUE>`| |Parameters related to source
`--out-param <KEY=VALUE>`| |Parameter related to target

## Examples

### Create a new descriptor file
```bash
./odm-cli local import -f dpd/data-product-descriptor.json \
      --from sql-ddl --to output-port \
      --in-param file=test.sql \
      --out-param name=oport123 \
      --out-param database=odmdb
```



