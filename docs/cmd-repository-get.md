# odmcli repository get

## Usage

`odmcli repository get`

## Description

Get an enetity from the repository

**⚒️Implementation details:**

1. TODO

## Options

Option|Default|Description
-------|----------|-------
`-f, --file`|PATH/data-product-descriptor.json|Name of the descriptor file
`--type`||Type of the entity to get (dp|dpv)
`--id`||id of the enetity to get
`--dp-version`||version of the dp

## Examples

### Get data product 123
```bash
./odmcli registry get --type dp --id 123
```

### Get version 2.0.0 of data product 123
```bash
./odmcli registry get --type dpv --id 123 --dp-version 2.0.0
```
