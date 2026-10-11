$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing
$dir = "C:\Users\User\Desktop\SulfurCraft\sulfurcraft\mdk\src\main\resources\assets\sulfurcraft\textures\block"
New-Item -ItemType Directory -Force -Path $dir | Out-Null

function New-Tex([string]$name, [byte[]]$data) {
  $bmp = New-Object System.Drawing.Bitmap(16, 16)
  for ($y = 0; $y -lt 16; $y++) {
    for ($x = 0; $x -lt 16; $x++) {
      $i = ($y * 16 + $x) * 4
      $r = $data[$i]; $g = $data[$i+1]; $b = $data[$i+2]; $a = $data[$i+3]
      $bmp.SetPixel($x, $y, [System.Drawing.Color]::FromArgb($a, $r, $g, $b))
    }
  }
  $bmp.Save("$dir\$name", [System.Drawing.Imaging.ImageFormat]::Png)
  $bmp.Dispose()
  "saved $name"
}

$rnd = New-Object System.Random(42)
function Jit([int]$v, [int]$amp) {
  $n = $rnd.Next(-$amp, $amp + 1)
  $r = [Math]::Min(255, [Math]::Max(0, $v + $n))
  return [byte]$r
}

# ---------- fryer_metal.png : dark gray metal panels with rivets ----------
$data = New-Object 'System.Byte[]' (16*16*4)
for ($y = 0; $y -lt 16; $y++) {
  for ($x = 0; $x -lt 16; $x++) {
    $i = ($y * 16 + $x) * 4
    # base dark metal
    $v = 72 + ($rnd.Next(-6, 7))
    if ($y -eq 0 -or $y -eq 15) { $v = 92 }          # top/bottom edge highlight
    if ($x -eq 0 -or $x -eq 15) { $v = 84 }
    if ($y -eq 7) { $v = 55 }                        # horizontal seam
    if (($x -eq 3 -or $x -eq 12) -and (($y -eq 2) -or ($y -eq 12))) { $v = 120 }  # rivets
    $data[$i]   = $v; $data[$i+1] = $v; $data[$i+2] = $v + 6; $data[$i+3] = 255
  }
}
New-Tex 'fryer_metal.png' $data

# ---------- fryer_oil.png : amber oil surface with sheen ----------
$data = New-Object 'System.Byte[]' (16*16*4)
for ($y = 0; $y -lt 16; $y++) {
  for ($x = 0; $x -lt 16; $x++) {
    $i = ($y * 16 + $x) * 4
    # amber oil gradient: darker at edges
    $edge = [Math]::Min([Math]::Min($x, 15 - $x), [Math]::Min($y, 15 - $y))
    $r = 232; $g = 154; $b = 38
    if ($edge -eq 0) { $r = 190; $g = 120; $b = 24 }
    elseif ($edge -eq 1) { $r = 214; $g = 138; $b = 30 }
    # sheen diagonal
    if (($x + $y) -ge 8 -and ($x + $y) -le 10 -and $x -gt 3 -and $x -lt 13) { $r = 255; $g = 198; $b = 84 }
    # subtle bubbles
    if (($x -eq 5 -and $y -eq 11) -or ($x -eq 11 -and $y -eq 5) -or ($x -eq 9 -and $y -eq 9)) { $r = 250; $g = 176; $b = 52 }
    $n = $rnd.Next(-6, 7)
    $data[$i]   = [Math]::Min(255, [Math]::Max(0, $r + $n))
    $data[$i+1] = [Math]::Min(255, [Math]::Max(0, $g + $n))
    $data[$i+2] = [Math]::Min(255, [Math]::Max(0, $b + $n))
    $data[$i+3] = 255
  }
}
New-Tex 'fryer_oil.png' $data

# ---------- fryer_basket.png : metal grid basket ----------
$data = New-Object 'System.Byte[]' (16*16*4)
for ($y = 0; $y -lt 16; $y++) {
  for ($x = 0; $x -lt 16; $x++) {
    $i = ($y * 16 + $x) * 4
    $isBar = (($x % 4) -eq 0) -or (($y % 4) -eq 0)
    if ($isBar) {
      $v = 118 + $rnd.Next(-8, 9)   # shiny steel bars
      if (($x % 4) -eq 0 -and ($y % 4) -eq 0) { $v = 140 }
      $data[$i] = $v; $data[$i+1] = $v; $data[$i+2] = $v + 4
    } else {
      $data[$i] = 0; $data[$i+1] = 0; $data[$i+2] = 0
    }
    $data[$i+3] = 255
  }
}
New-Tex 'fryer_basket.png' $data

# ---------- fryer_handle.png : dark handle ----------
$data = New-Object 'System.Byte[]' (16*16*4)
for ($y = 0; $y -lt 16; $y++) {
  for ($x = 0; $x -lt 16; $x++) {
    $i = ($y * 16 + $x) * 4
    $v = 96 + $rnd.Next(-5, 6)
    if ($y -ge 6 -and $y -le 9) { $v = 130 }  # grip band
    $data[$i] = $v; $data[$i+1] = $v; $data[$i+2] = $v + 5; $data[$i+3] = 255
  }
}
New-Tex 'fryer_handle.png' $data
