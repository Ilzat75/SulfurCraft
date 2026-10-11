Add-Type -AssemblyName System.IO.Compression.FileSystem
$z = [System.IO.Compression.ZipFile]::OpenRead("C:\Users\User\Desktop\SulfurCraft\sulfurcraft-1.2.jar")
$names = $z.Entries | ForEach-Object { $_.FullName }
Write-Host "--- class count ---"
($names | Where-Object { $_ -like "*.class" }).Count
Write-Host "--- BEFactory + AbstractMachine ---"
$names | Where-Object { $_ -match "BEFactory|AbstractMachine|MachineBlock|FryerMenu" }
$z.Dispose()
