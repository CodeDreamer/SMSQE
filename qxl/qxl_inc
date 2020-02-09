; QXL Interface Keys
; 2004.01.01    v. 1.01 added keys for drive choice (BC)
; 2004.01.10    V. 1.02 ADDED KEY FOR QXL RESTART (BC)

baseseg SEGMENT AT 0
        ORG 4*8
int_timi WORD   ?   ; timer interrupt instruction
int_tims WORD   ?   ; and segment
int_kbdi WORD   ?   ; keyboard interrupt instruction
int_kbds WORD   ?   ; and segment
         WORD   ?
         WORD   ?
int_rq3i WORD   ?   ; IRQ3 COM2 (or 4) instruction
int_rq3s WORD   ?   ; and segment
int_rq4i WORD   ?   ; IRQ4 COM1 (or 3) instruction
int_rq4s WORD   ?   ; and segment
        ORG 4*0fh
int_rq7i WORD   ?   ; IRQ7 LPT1 instruction
int_rq7s WORD   ?   ; and segment
        ORG 4*1bh
int_brki WORD   ?   ; break instruction
int_brks WORD   ?   ; break segment
        ORG 4*1eh
int_dbi  WORD   ?   ; disk base instruction
int_dbs  WORD   ?   ; disk base segment
        ORG 4*033h
int_msei WORD   ?   ; mouse interrupt instruction
int_mses WORD   ?   ; mouse interrupt segment
        ORG 0400h
add_com1 WORD   ?   ; COM1 port address
         WORD   ?
         WORD   ?
         WORD   ?
add_lpt1 WORD   ?   ; LPT1 port address
        ORG 0497h
kbd_byt2 BYTE   ?   ; keyboard status byte 2
baseseg ENDS

; SMSQ file header 'constants'
smsq_qxl_base = 004h
smsq_len      = 020h
smsq_qxl_drl  = 024h

pic_ctrl = 020h     ; programmable interrupt controller
pic_eof  = 020h     ; end of interrupt
pic_mask = 021h     ; interrupt mask
tim_dat0 = 040h     ; timer 0 data
tim_rate = 2000     ; timer interrupt rate
tim_time = 254h     ; 500us
tim_timl = 054h     ; time low byte for $254=500us
tim_timh = 002h     ; time high byte
tim_dat2 = 042h     ; timer 2 data
tim_ctrl = 043h     ; timer control
tim_sqr0 = 036h     ; square wave timer 0
tim_sqr2 = 0b6h     ; square wave timer 2

prt_datb = 061h     ; port b data

kbd_data = 060h     ; keyboard controller data register
kbd_prog = 064h     ; keyboard controller prog register
kbd_enab = 0AEh     ; enable keyboard code (port 64h)
kbd_disa = 0ADh     ; disable keyboard code (port 64h)
kbd_ldup = 0EDh     ; led update code (port 60h)

kbd_num  = 02h      ; num lock bit (40:97)
kbd_mask = 07h

lpt_stblow =  0Dh   ; strobe
lpt_stbhigh = 0ch

com_data = 0
com_ier  = 1
com_ire    = 001h
com_iir  = 2
com_imi    = 001h
com_fic  = 2
com_fiz    = 007h ; 0 threshold fifo set
com_lcr  = 3
com_lcr8   = 007h       ; 8 bit 2 stop
com_dlab   = 087h ; access divisor latch
com_mcr  = 4
com_rts    = 002h
com_erts   = 00bh ; rts / dtr / interrupts enabled
com_nrts   = 008h ; interrupts enabled - no rts
com_lsr  = 5
com_rdr    = 001h
com_thr    = 020h
com_tsr    = 040h
com_msr  = 6
com_cts    = 010h

buff_stt = 0   ; (pointer to) start of buffer
buff_put = 2   ; put pointer
buff_get = 4   ; get pointer
buff_end = 6   ; end of buffer
buff_start = 8 ; start of buffer

qxm_setup  = 01h        ; PC setup message
qxm_flowqx = 02h  ; QXL - PC flow control
qxm_flowpc = 03h  ; PC - QXL flow control
qxm_srtc   = 04h  ; Set RTC
qxm_rtcu   = 05h  ; RTC updated
qxm_kbdd   = 07h        ; send keyboard data
qxm_vmode  = 08h        ; set VGA mode
qxm_vmack  = 09h        ; acknowledge VGA mode set
qxm_rstrt  = 0Bh        ; QXL restart
qxmd_ppl   = 6  ; VGA pixel plane mode
qxmd_pkp   = 7  ; VESA packed pixel mode
qxm_rxdata = 17h  ; data read
qxm_rphys  = 18h        ; read physical sector
qxm_physr  = 19h        ; physical sector read
qxm_rpfail = 1Bh        ; read physical fail
qxm_wphys  = 1Ch        ; write physical sector
qxm_physw  = 1Dh        ; physical sector written
qxm_wpfail = 1Fh        ; write physical fail
qxm_fdriv  = 30h        ; format drive
qxm_drivf  = 31h        ; drive formatted
qxm_mouse  = 33h        ; mouse update
