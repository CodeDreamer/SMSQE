; SMS2 IOSS operations
        nolist

do.io    equ    3     ; trap #3
do.relio equ    4     ; trap #4 

; character/byte IO operations

iob.test equ    $00   ; TEST input
iob.fbyt equ    $01   ; Fetch BYTe from input
iob.flin equ    $02   ; Fetch LINe from input
iob.fmul equ    $03   ; Fetch MULtiple characters/bytes
iob.elin equ    $04   ; Edit LINe of characters
iob.sbyt equ    $05   ; Send BYTe to output
iob.suml equ    $06   ; Send Untranslated MuLtiple bytes
iob.smul equ    $07   ; Send MULtiple bytes

; window IO operations

iow.xtop equ    $09   ; eXTernal OPeration on screen
iow.pixq equ    $0a   ; PIXel coordinate Query
iow.chrq equ    $0b   ; CHaRacter coordinate Query
iow.defb equ    $0c   ; DEFine Border
iow.defw equ    $0d   ; DEFine Window
iow.ecur equ    $0e   ; Enable CURsor
iow.dcur equ    $0f   ; Disable CURsor
iow.scur equ    $10   ; Set CURsor position (character coordinates)
iow.scol equ    $11   ; Set cursor COLumn
iow.newl equ    $12   ; put cursor on a NEW Line
iow.pcol equ    $13   ; move cursor to Previous COLumn
iow.ncol equ    $14   ; move cursor to Next COLumn
iow.prow equ    $15   ; move cursor to Prevous ROW
iow.nrow equ    $16   ; move cursor to Next ROW
iow.spix equ    $17   ; Set cursor to PIXel position
iow.scra equ    $18   ; SCRoll All of window
iow.scrt equ    $19   ; SCRoll Top of window (above cursor)
iow.scrb equ    $1a   ; SCRoll Bottom of window (below cursor)
iow.pana equ    $1b   ; PAN All of window

iow.panl equ    $1e   ; PAN cursor Line
iow.panr equ    $1f   ; PAN Right hand end of cursor line
iow.clra equ    $20   ; CLeaR All of window
iow.clrt equ    $21   ; CLeaR Top of window (above cursor)
iow.clrb equ    $22   ; CLeaR Bottom of window (below cursor)
iow.clrl equ    $23   ; CLeaR cursor Line
iow.clrr equ    $24   ; CLeaR Right hand side of cursor line
iow.font equ    $25   ; set / read FOuNT (font U.S.A.)
iow.rclr equ    $26   ; ReCoLouR a window
iow.spap equ    $27   ; Set PAPer colour
iow.sstr equ    $28   ; Set STRip colour
iow.sink equ    $29   ; Set INK colour
iow.sfla equ    $2a   ; Set FLash Attribute
iow.sula equ    $2b   ; Set UnderLine Attribute
iow.sova equ    $2c   ; Set OVerwrite Attributes
iow.ssiz equ    $2d   ; Set character SIZe
iow.blok equ    $2e   ; fill a BLOcK with colour

iow.donl equ    $2f   ; DO a pending newline

; graphics operations

iog.dot  equ    $30   ; draw (list of) DOTs
iog.line equ    $31   ; draw (list of) LINEs
iog.arc  equ    $32   ; draw (list of) ARCs
iog.elip equ    $33   ; draw ELlIPse
iog.scal equ    $34   ; set graphics SCALe
iog.fill equ    $35   ; set area FILL
iog.sgcr equ    $36   ; Set Graphics CuRsor position


; filing system

iof.chek equ    $40   ; CHEcK all pending operations on file
iof.flsh equ    $41   ; FLuSH all buffers
iof.posa equ    $42   ; set file POSition to Absolute address
iofp.off equ      $F0FFF0FF ; key in d1, returns sector 0 offset (direct sect)
iofp.def equ      $F0FFF100 ; key in d1, returns -1 in d1 if next read is IDE
                            ;            drive definition sector (real or fake)
iof.posr equ    $43   ; move file POSition Relative to current position
iof.posv equ    $44   ; set file POSition to Virtual address
iof.minf equ    $45   ; get Medium INFormation
iof.shdr equ    $46   ; Set file HeaDeR
iof.rhdr equ    $47   ; Read file HeaDeR
iof.load equ    $48   ; (scatter) LOAD file
iof.save equ    $49   ; (scatter) SAVE file
iof.rnam equ    $4a   ; ReNAMe file
iof.trnc equ    $4b   ; TRuNCate file to current position
iof.date equ    $4c   ; set or get file DATEs
iofd.get equ      -1    ; d1 key, GET date (or version)
iofd.cur equ      0     ; d1 key, set CURrent date (or version) 
iofd.upd equ      0     ; d2 key, set/get UPDate date  
iofd.bak equ      2     ; d2 key, set/get BAcKup date
iof.mkdr equ    $4d   ; MaKe DiRectory
iof.vers equ    $4e   ; set or get VERSion (d1 keys as iof.date)
iof.xinf equ    $4f   ; get eXtended INFormation
ioi_name equ      $00   ; string  up to 20 character medium name (null filled)
ioi_dnam equ      $16   ; string  up to 4 character long device name (e.g. WIN)
ioi_dnum equ      $1c   ; byte    drive number
ioi_rdon equ      $1d   ; byte    set if read only
ioi_allc equ      $1e   ; word    allocation unit size (in bytes)
ioi_totl equ      $20   ; long    total medium size (in allocation units)
ioi_free equ      $24   ; long    free space on medium (in allocation units)
ioi_hdrl equ      $28   ; long    header length (per file storage overhead)
ioi_ftyp equ      $2c   ; byte    format type (1=QDOS, 2=MSDOS etc)
ioi_styp equ      $2d   ; byte    format sub-type
ioi_dens equ      $2e   ; byte    density
ioi_mtyp equ      $2f   ; byte    medium type (RAM=0, FLP=1, HD=2, CD=3)
ioi_remv equ      $30   ; byte    set if removable
ioi_xxx1 equ      $31   ; $0f     bytes set to -1
ioi.blkl equ      $40   ; information block length

;       Extended colour traps

iow.papp equ    $50   ; define paper colour (palette)
iow.strp equ    $51   ; define strip colour (palette)
iow.inkp equ    $52   ; define ink colour (palette)
iow.borp equ    $53   ; define border (palette)

iow.papt equ    $54   ; define paper colour (24 bit)
iow.strt equ    $55   ; define strip colour (24 bit)
iow.inkt equ    $56   ; define ink colour (24 bit)
iow.bort equ    $57   ; define border (24 bit)

iow.papn equ    $58   ; define paper colour (native)
iow.strn equ    $59   ; define strip colour (native)
iow.inkn equ    $5a   ; define ink colour (native)
iow.born equ    $5b   ; define border (native)

iow.blkp equ    $5c   ; draw block (palette)
iow.blkt equ    $5d   ; draw block (24 bit)
iow.blkn equ    $5e   ; draw block (native)

iow.palq equ    $60   ; define QL colour palette
iow.palt equ    $61   ; define 8 bit palette

iow.salp equ    $62   ; set alpha blending weight

;       Pointer I/O trap keys

iop.wpap equ    $6b   ; define wallpaper
iop.flim equ    $6c   ; Find window LIMits
iop.svpw equ    $6d   ; SaVe Part of Window
iop.rspw equ    $6e   ; ReStore Part of Window
iop.slnk equ    $6f   ; Set bytes in LiNKage block
iop.pinf equ    $70   ; pointer information
iop.rptr equ    $71   ; read pointer
iop.rpxl equ    $72   ; read pixel
iop..gcl equ      16  ;   use given colour
iop..sdr equ      17  ;   scan down right
iop..slr equ      18  ;   scan left right
iop..ssc equ      19  ;   scan same colour
iop..scn equ      31  ;   scan
iop.wblb equ    $73   ; write blob
iop.lblb equ    $74   ; write line of blobs

iop.wspt equ    $76   ; write sprite
iop.spry equ    $77   ; spray pixels
iop.film equ    $78   ; fill within mask
iop.splm equ    $79   ; set pointer limits
iop.outl equ    $7a   ; set window outline
iopo.set equ      0   ;   just set     
iopo.mov equ      1   ;   move contents and set outline
iop.sptr equ    $7b   ; set pointer position
iops.rew equ      -1  ;   pointer position relative to window
iops.abs equ      0   ;   pointer position absolute
iops.reh equ      1   ;   pointer position relative to hit area
iop.pick equ    $7c   ; pick / bury window
iopp.bot equ      -1  ;   pick bottom
iopp.nlk equ      -2  ;   set no lock
iopp.frz equ      -3  ;   set freeze

iop.swdf equ    $7d   ; set window definition
iop.wsav equ    $7e   ; locate and save window
iop.wrst equ    $7f   ; restore window

; timeout keys

no.wait  equ    0
forever  equ    -1
        list
