# odmcli local init

## Usage

`odmcli local init`

## Description

Initializes a descriptor file. 

**⚒️Implementation details:**

1. 

## Options

Option|Default|Description
-------|----------|-------
`-f, --file`|PATH/data-product-descriptor.json|Name of the descriptor file
`--init-param <KEY=VALUE>`| |Initialization parameters

## Examples

### Create a new descriptor file
```bash
./odmcli local init -f dpd/data-product-descriptor.json \
     --init-param product-name=product123 \
     --init-param info.owner.id=john.doe@newco.com
```



