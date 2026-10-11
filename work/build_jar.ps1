param([string]$StageDir, [string]$OutJar)

$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$root = 'C:\Users\User\Desktop\SulfurCraft'
$stageFull = (Resolve-Path (Join-Path $root $StageDir)).Path.TrimEnd('\') + '\'
$outFull = Join-Path $root $OutJar

Write-Host "stage=$stageFull"
Write-Host "out=$outFull"

if (Test-Path $outFull) { Remove-Item $outFull -Force }

$zip = [System.IO.Compression.ZipFile]::Open($outFull, [System.IO.Compression.ZipArchiveMode]::Create)
$bad = 0
$files = Get-ChildItem -Path $stageFull -Recurse -File
foreach ($f in $files) {
    $rel = $f.FullName.Substring($stageFull.Length).Replace('\', '/')
    if ($rel.Contains('\')) { $bad++ }
    $entry = $zip.CreateEntry($rel, [System.IO.Compression.CompressionLevel]::Optimal)
    $stream = $entry.Open()
    $fs = [System.IO.File]::OpenRead($f.FullName)
    $fs.CopyTo($stream)
    $fs.Close()
    $stream.Close()
}
$zip.Dispose()
$size = (Get-Item $outFull).Length
Write-Host "entries=$($files.Count) bad=$bad size=$size"
