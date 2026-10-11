Add-Type -AssemblyName System.Drawing
$res = "C:\Users\User\Desktop\SulfurCraft\sulfurcraft\mdk\src\main\resources\assets\sulfurcraft\textures\gui\container"
New-Item -ItemType Directory -Force -Path $res | Out-Null

$bmp = New-Object System.Drawing.Bitmap(176, 166)
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.SmoothingMode = "None"

function ARGB($r,$gg,$b) { return [System.Drawing.Color]::FromArgb(255,$r,$gg,$b) }
function Fill($x,$y,$w,$h,$col) { $g.FillRectangle((New-Object System.Drawing.SolidBrush($col)), $x, $y, $w, $h) }
function Rect($x,$y,$w,$h,$col) { $g.DrawRectangle((New-Object System.Drawing.Pen($col)), $x, $y, $w-1, $h-1) }
function Slot($x,$y) {
   Fill $x $y 18 18 (ARGB 139 139 139)
   Fill $x $y 18 1 (ARGB 55 55 55)
   Fill $x $y 1 18 (ARGB 55 55 55)
   Fill ($x+17) $y 1 18 (ARGB 255 255 255)
   Fill $x ($y+17) 18 1 (ARGB 255 255 255)
}

# panel background (furnace gray)
Fill 0 0 176 166 (ARGB 198 198 198)
# outer border light top-left, dark bottom-right
Fill 0 0 176 1 (ARGB 255 255 255)
Fill 0 0 1 166 (ARGB 255 255 255)
Fill 0 165 176 1 (ARGB 55 55 55)
Fill 175 0 1 166 (ARGB 55 55 55)
Fill 1 1 174 1 (ARGB 255 255 255)
Fill 1 1 1 164 (ARGB 255 255 255)

# machine slots
Slot 38 17
Slot 56 17
Slot 56 53
Slot 116 35

# fire box background (below in2, above fuel) 14x14 at (56,36)
Fill 56 36 14 14 (ARGB 60 60 60)
Rect 56 36 14 14 (ARGB 30 30 30)

# arrow background (right-pointing) at (79,34) 24x12
$arrowPts = @(
   (New-Object System.Drawing.Point(79,37)),
   (New-Object System.Drawing.Point(95,37)),
   (New-Object System.Drawing.Point(95,33)),
   (New-Object System.Drawing.Point(103,40)),
   (New-Object System.Drawing.Point(95,47)),
   (New-Object System.Drawing.Point(95,43)),
   (New-Object System.Drawing.Point(79,43))
)
$g.FillPolygon((New-Object System.Drawing.SolidBrush(ARGB 120 120 120)), $arrowPts)

# gauge background (oil/batch) 24x6 at (79,52)
Fill 79 52 24 6 (ARGB 60 60 60)
Rect 79 52 24 6 (ARGB 30 30 30)

# player main inventory 3x9 at (8,84)
for ($r=0; $r -lt 3; $r++) { for ($c=0; $c -lt 9; $c++) { Slot (8 + $c*18) (84 + $r*18) } }
# hotbar at (8,142)
for ($c=0; $c -lt 9; $c++) { Slot (8 + $c*18) 142 }

$bmp.Save((Join-Path $res "machine.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$g.Dispose(); $bmp.Dispose()
Write-Host "GUI saved"
