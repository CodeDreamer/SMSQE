; Initialised data definitions
; 2006.10.01	1.01    uses standard segmentation directives, kbd_lick added,  BOOTLOADER data added,uninitialized data moved to qxl_bss.asm(BC)

_data   SEGMENT DWORD PUBLIC 'DATA'

psp             WORD    0                               ; my program segment prefix (segment number)
qxl             WORD    0                               ; base address of QXL
qxl_code        WORD    0                               ; base address of QXL code
addr_flid   WORD  0                             ; QXL.DAT card address file ID
addr_flnew      BYTE  0                         ; new QXL.DAT file
loadf           BYTE    0ffh                            ; load required

dispm           BYTE    010h                            ; initial display mode
dispc           BYTE    0                               ; initial display colour depth
lpt_name    BYTE        "LPT1",0,0
com_name        BYTE  "COM1",0,0
lpt_maxport WORD  0                             ; highest port number 1 to n
lpt_notbusy BYTE        0                               ; set if data can be transferred in main loop
lpt_txdata      BYTE    0                               ; set if there is data to send
lpt_n           WORD    8 DUP (0)                       ; LPT port addresses (if NZ)
lpt_buff        WORD    8 DUP (0)                       ; LPT buffer addresses (if NZ)
lpt_room    WORD  8 DUP (0)                     ; LPT buffer room
com_maxport WORD  0                             ; highest port number 1 to n
com_notbusy BYTE        0                               ; set if data can be transferred in main loop
com_txdata      BYTE    0                               ; set if there is data to send
com_n           WORD    16 DUP (0)                      ; COM port addresses (if NZ)
com_ibuff       WORD    16 DUP (0)                      ; COM input buffer addresses (if NZ)
com_obuff       WORD    16 DUP (0)                      ; COM output buffer addresses (if NZ)
com_room    WORD  16 DUP (0)                    ; COM buffer room
com_rxdata      BYTE    32 DUP (0)                      ; set if there is received data
com_rxoff   = com_rxdata+1                      ; set +ve if RTS is off, -ve if closed

drf_drive       BYTE    "*AB"
drf_drl         BYTE    "CDEFGHIJKLMNOP*"
drf_name        BYTE    "A:\QXL.WIN",0,0
drf_ID          WORD    26 DUP (0)                      ; ID of QXL.DAT file

mse_port        BYTE  0ffh                              ; mouse "COM" port offset (0-1), -1 none, $80 bus
                BYTE  0
mse_save        WORD    0,0,0,0                 ; mouse save area

wrk_buff        BYTE    80 DUP (0)
vector_tab  WORD  40*3 DUP (0)          ; up to 40 vectors set
vector_tabp WORD  OFFSET vector_tab
hw_tab  BYTE  40*3 DUP (0)              ; up to 40 IO addresses set
hw_tabp WORD  OFFSET hw_tab
qts_sttop   BYTE  1024 DUP (055h)               ; timer server stack
qts_stack       WORD  05555h

bad_para        BYTE    " is not a valid QXL address",0
the_qxl         BYTE    "The QXL at IO address ",0,0
no_reply        BYTE    " is not responding",0,0
no_file         BYTE    "Cannot access file ",0
QXL_DAT         BYTE    'QXL.DAT',0
crlf            BYTE  13,10
nul             BYTE    0,0

comm_count  WORD        0
comm_junk   WORD  0,0,0,0,0,0,0,0

; Timer definitions

timer_cnt       WORD    0                               ; counter used to simulate normal PC timer
sound_cnt       WORD    0                               ; counter used to simulate QL sound
qxl_busy        WORD    0                               ; QXL busy counter
qxl_maxbusy = 10*tim_rate                       ; 10 seconds
qxl_tick        WORD  0                         ; timer tick counter

; Keyboard definitions

kbd_lock        BYTE    0                               ; KBD lock status
kbd_llck        BYTE    0                               ; last KBD lock status
kbd_save        BYTE    0                               ; last keystroke
kbd_stop        BYTE    0                               ; set to stop
                BYTE    0
kbd_buffsz = 512
kbd_buff        WORD    OFFSET kbd_buffd, OFFSET kbd_buffd, OFFSET kbd_buffd, OFFSET kbd_buffd+kbd_buffsz
kbd_buffd       BYTE  kbd_buffsz dup (0)

; Sound definitions

qsd_stat    WORD        0       ; beep status 0 idle, +ve start, -ve run
qsd_high    WORD    2000        ; high pitch
qsd_low     WORD    4000        ; low pitch
qsd_step    WORD    200 ; step in pitch
qsd_int     WORD    1   ; interval
qsd_wrap    WORD    2   ; wrap

qsd_pitch   WORD    0   ; current pitch
qsd_icnt    WORD    0   ; interval counter
qsd_wcnt    WORD    0   ; wrap counter

; Video definitions

vmod_set    WORD    0ffffh  ; >=0 set if video mode to be set

            WORD    0

; Setup message

        ALIGN 4

                WORD    0       ; dummy word for alignment

mes1_head       WORD    0       ; message 1 setup buffer
                BYTE    qxm_setup
mes1_lpt        BYTE    0       ; LPT ports
mes1_com        WORD    0       ; COM ports
mes1_pxp        BYTE    03h     ; VGA 16 colour default 640x480 and 800x600 (modes 12h and 6ah)
mes1_dv8        BYTE    00h     ; SVGA 256 colour packed pixel only
mes1_dvh        BYTE    00h     ; SVGA 65536 colour direct write = colour packed pixel only
mes1_dvf        BYTE    00h     ; SVGA 16M colour direct write = colour packed pixel only
mes1_vmode      = mes1_pxp  ; table of VESA modes

; Flow control message (bits set for OK)

        ALIGN 4

                WORD    0       ; dummy word for alignment

flowpc_head     WORD    0       ; flow control from PC setup buffer
flowpc_len  = 8
                BYTE    qxm_flowpc
flowpc_lpt      BYTE    0       ; LPT ports
flowpc_com      WORD    0       ; COM ports
flowpc_atop WORD        0       ; drives a to p
flowpc_msbf BYTE  0     ; message buffers
flowpc_kbd  BYTE  0     ; keyboard status

        ALIGN 4

flowqx_mess     BYTE    0       ; flow control from QXL
flowqx_lpt      BYTE    0       ; LPT ports = 0!!
flowqx_com      WORD    0       ; COM ports
flowqx_atop WORD        0       ; drives a to p = 0!!
flowqx_msbf BYTE  0     ; message buffers = 0!!
flowqx_kbd  BYTE  0     ; keyboard status = 0!!

; VGA ack message

        ALIGN 4

                WORD    0       ; dummy word for alignment

mes_vmack       WORD    0       ; Display mode acknowledge message
                BYTE    qxm_vmack,0
                WORD    -1      ; to pad to 4 bytes

; QXL restart message

        ALIGN 4

                WORD    0       ; dummy word for alignment

mes_rstrt       WORD    0       ; QXL restart message
                BYTE    qxm_rstrt,0
                WORD    -1      ; to pad to 4 bytes
                BYTE    qxm_kbdd
                BYTE    1       ; 1 keyboard data
                BYTE    9dh     ; Ctrl key released
                BYTE    -1      ; to pad to 4 bytes

; RTC message

        ALIGN 4

                WORD    0       ; dummy word for alignment

rtc_mlen        WORD    0       ; message length
rtc_mcmd        BYTE    0       ; rtc message command byte
rtc_year        BYTE  0 ; year (from 1980)
rtc_month       BYTE    0       ; month
rtc_day BYTE  0 ; day
rtc_dow BYTE    0       ; day of week (not set)
rtc_hour        BYTE    0       ; hour
rtc_minute      BYTE    0       ; minute
rtc_second      BYTE    0       ; second

; Mouse message

        ALIGN 4

                WORD    0       ; dummy word for alignment

mse_mlen        WORD    0       ; mouse message length
mse_mcmd        BYTE    qxm_mouse
mse_button      BYTE    0       ; buttons pressed
mse_dx  WORD    0       ; X displacement
mse_dy  WORD    0       ; Y displacement
                WORD    -1      ; to pad to 4 bytes

mse_sx  WORD    0       ; saved X position
mse_sy  WORD    0       ; saved Y position

; Physical device area

; In this version, the system only handles one read and one write sector operation
; at a time

phd_rsectls WORD        0       ; read sector number
phd_rsectms     WORD    0
phd_rlen        WORD  0 ; length of command (0 nothing to do, +ve acknowledge, -ve request)
phd_rcmd    BYTE  0     ; command
phd_rdrive      BYTE    0       ; physical device number (or 0 or error code)
phd_rerror = phd_rdrive
phd_rsern       WORD    0       ; serial transaction number
phd_rdata       BYTE  512 dup (0)

phd_wsectls WORD        0       ; read sector number
phd_wsectms     WORD    0
phd_wlen        WORD  0 ; length of command (0 nothing to do, +ve acknowledge, -ve request)
phd_wcmd    BYTE  0     ; command
phd_wdrive      BYTE    0       ; physical device number (or 0 or error code)
phd_werror = phd_wdrive
phd_wsern       WORD    0       ; serial transaction number

        ALIGN 4

phd_wdata       BYTE  512 dup (0)
phd_wntrk  = phd_wdata + 1 ; number of tracks
phd_wstrk  = phd_wdata + 3 ; sectors per track

phd_sectls = phd_rsectls - phd_rcmd
phd_sectms = phd_rsectms - phd_rcmd
phd_drive  = phd_rdrive  - phd_rcmd

; VESA comms area (word + 512+256 bytes)

vesa_wind       WORD  -1        ; current VESA window offset
vesa_save       WORD    0       ; VGA / VESA save segment
vesa_info       BYTE  "VBE2"; VESA info buffer
vesa_vers       WORD    0       ; VESA version
vesa_eom        WORD    0,0     ; manufacturer
vesa_cap        WORD    0,0     ; capabilities
vesa_mptr   WORD        0,0     ; table of modes
vesa_mem        WORD    0       ; 64k memory blocks
vesa_srev       WORD    0       ; software revision
vesa_vend       WORD    0,0     ; vendor name
vesa_prod       WORD    0,0     ; product name
vesa_prev       WORD    0,0     ; product revision
                WORD    256-17 dup (01234h)

vesa_mattr      WORD    0       ; mode attributes
vesa_mwaa       BYTE    0       ; window a attributes
vesa_mwab       BYTE    0       ; window b attributes
vesa_mgran      WORD    0       ; window offset granularity in 1kbyte units
vesa_msize      WORD    0       ; window size in 1kbyte units
vesa_msega      WORD    0       ; window a segment
vesa_msegb      WORD    0       ; window b segment
vesa_mcall      WORD    0,0     ; address of window control routine
vesa_mbpl       WORD  0 ; bytes per scan line
vesa_mhpix      WORD    0       ; horizontal pixels
                BYTE  256-17 DUP (056h)
vesa_grsft      BYTE    0       ; granularity shift to 64 kbytes

vesa_mset       BYTE    000h,002h,004h,006h ; (16 colour) packed pixel
                BYTE    010h,013h,016h,019h ; 256 colours !!!! (guesswork)
                BYTE    011h,014h,017h,01ah ; 65536 colours
                BYTE    012h,015h,018h,01bh ; 15 M colours

vesa_mode       BYTE    00h,00h,02h,00h, 04h,00h,08h,00h, 0,0,0,0,        0,0,0,0
                BYTE    01h,01h,01h,02h, 02h,02h,04h,04h, 04h,08h,08h,08h
vesa_colr       BYTE    00h,00h,00h,00h, 00h,00h,00h,00h, 0,0,0,0,        0,0,0,0
                BYTE    01h,02h,03h,01h, 02h,03h,01h,02h, 03h,01h,02h,03h
vesa_max = 01bh

; buffer area

qxl_smqui       WORD OFFSET qxl_smqu
qxl_smquo       WORD OFFSET qxl_smqu
qxl_smqu        WORD 64 DUP (0) ; queue of pointers to messages to send
qxl_smqutop WORD        0

qxl_pal4        BYTE    00h,24h,12h,3fh,00h,24h,12h,3fh
                BYTE    00h,24h,12h,3fh,00h,24h,12h,3fh
                BYTE    00h
qxl_pal8        BYTE    00h,24h,12h,36h,09h,2dh,1bh,3fh
                BYTE    00h,24h,12h,36h,09h,2dh,1bh,3fh
                BYTE    0

qxl_sbuffp      WORD OFFSET qxl_sbuffer
qxl_rbuffp  WORD OFFSET qxl_rbuffer

qbl_retry       WORD    5
qbl_sect        WORD    0

QXL_JMP  DWORD  04ef80020h              ; jmp $20
QXL_NOP  DWORD  070007000h              ; moveq #0,d0; moveq #0,d0
         DWORD  070007000h              ; moveq #0,d0; moveq #0,d0

QXL_RESV DWORD  000000020h              ; reset vectors
         DWORD  000000020h              ; reset vectors

QXL_LBAS DWORD  043f80020h              ; lea $20,a1
         DWORD  022802280h              ; move.l d0,(a1); move.l d0,(a1)
QXL_MOVE DWORD  0700022fch              ; move.l #xxxx,(a1)+
QXL_LSTT DWORD  0700043f9h              ; moveq #0,d0; lea $800000,a1
         DWORD  000800000h
QXL_STT  DWORD  04a114a11h              ; tst.b (a1) (start)

QXL_LOAD WORD   24 DUP (07000h)         ; 24 moveq #0,d0
         DWORD  0780041f9h              ; moveq #0,d4; lea $900000,a0
         DWORD  000900000h
         DWORD  0780043f9h              ; moveq #0,d5; lea $30000,a1
         DWORD  000030000h
         DWORD  02e4945f9h              ; move.l a1,a7; lea $a00002,a2
         DWORD  000a00002h
         DWORD  07c0049f9h              ; moveq #0,d6; lea $c00000,a4
         DWORD  000c00000h
         DWORD  07e004bf9h              ; moveq #0,d7; lea $d00000,a5
         DWORD  000d00000h

         DWORD  034bc1234h              ; move.w #$1234,(a2)
  ;start
         DWORD  010100c00h              ; move.b (a0),d0; cmpi.b ....,d0
         DWORD  000f166f8h              ; #$f1; bne.s start
  ;sector
         DWORD  061241e00h              ; bsr.s load+2; move.b d0,d7
         DWORD  0611ee14fh              ; bsr.s load; lsl.w #8,d7
         DWORD  08e406710h              ; or.w d0,d7; beq.s done
  ;byte
         DWORD  07a006114h              ; moveq #0,d5; bsr.s load
         DWORD  012c0da40h              ; move.b d0,(a1)+; add.w d0,d5
         DWORD  053476ef6h              ; subq.w #1,d7; bgt.s byte

         DWORD  0348560e4h              ; move.w d5,(a2); bra.s sector

  ;done
         DWORD  034873487h              ; move.w d7,(a2); move.w d7,(a2)
         DWORD  04ed77000h              ; jmp (sp); moveq       #0,d0

  ;load
         DWORD  0348051d4h              ; move.w d0,(a2); sf (a4) (a5)
  ;waitz
         DWORD  0101066fch              ; move.b (a0),d0; bne.s waitz
         DWORD  0101066f8h              ; move.b (a0),d0; bne.s waitz

         DWORD  0348751d4h              ; move.w d7,(a2); sf (a4)
  ;wait
         DWORD  0101067fch              ; move.b (a0),d0; beq.s wait
         DWORD  0b01066f8h              ; cmp.b (a0),d0; bne.s wait

         DWORD  04a006b0ch              ; tst.b d0; bmi.s set
         DWORD  04a066608h              ; tst.b d6; bne.s set
         DWORD  055006a04h              ; subq.b #2,d0; bpl.s set
         DWORD  050c660dch              ; st d6; bra.s waitz
   ;set
         DWORD  051c64e75h              ; sf d6; rts


QXL_ELOAD BYTE  0,0

_data   ENDS
