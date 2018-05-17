* Screen Dump BASIC interface   V2.00       1987  Tony Tebby   QJUMP
*
        section exten
*
        xdef    sdump
        xdef    sdp_set
        xdef    sdp_key
        xdef    sdp_dev
*
        xref    ut_chget
        xref    ut_gxnm1
        xref    ut_gtint
        xref    ut_gtli1
*
        include dev8_keys_err
        include dev8_keys_qlv
        include dev8_keys_qdos_io
        include dev8_keys_qdos_ioa
        include dev8_keys_qdos_pt
*
sdump_d dc.w    5,'SDUMP'
*
frame   equ     8
sdump
        moveq   #0,d5                    ; assume screen dump
        cmp.l   a3,a5                   any parameters?
        beq.s   sdp_all                 ... no
        btst    #7,1(a6,a3.l)           hash?
        beq.s   sdp_defw                ... no
        bsr.l   ut_chget                get channel
        bne.s   sdb_rts                 oops
        cmp.l   a3,a5                   no parameters left, please
        bne.s   sdb_ipar
*
        lea     swdef(pc),a2
        move.l  (a6),a1
        moveq   #iow.xtop,d0
        moveq   #-1,d3
        trap    #4
        trap    #3                      get area
        tst.l   d0
        beq.s   sdp_do                  do it
        bra.s   sdb_rts                 ... oops
*
sdp_defw
        move.l  a5,d7                    ; number of parameters
        sub.l   a3,d7
        bclr    #3,d7                    ; odd?
        beq.s   sdp_wind                 ; ... no
        jsr     ut_gtli1                 ; get one long integer
        bne.s   sdb_rts
        move.l  (a6,a1.l),a4             ; save area flag
        cmp.w   #ptp.flag,(a4)           ; is it?
        bne.s   sdb_ipar                 ; ... no
        subq.l  #ptp_flag,a4             ; backspace to start
        moveq   #1,d5                    ; whole save area
        tst.w   d7                       ; is it?
        beq.s   sdp_do                   ; ... do dump
        moveq   #2,d5                    ; partial partial
        addq.l  #8,a3                    ; next
sdp_wind
        jsr     ut_gtint                 ; get integers
        bne.s   sdb_rts
        subq.w  #4,d3                    ; 4 of them
        beq.s   sdp_do
*
sdb_ipar
        moveq   #err.ipar,d0
sdb_rts
        rts
*       
sdp_all
        move.l  (a6),a1                 into buffer
        clr.l   (a6,a1.l)
        clr.l   4(a6,a1.l)
*
sdp_do 
        moveq   #iow.defw,d7            set define window
        bra.s   sdb_do
        page
*
* set parameters
*
sdp_set
        jsr     ut_gtint                 ; get integers
        bne.s   sdb_rts
        tst.w   d3                       ; any parameters
        beq.s   sdb_rts                  ; ... no
        move.w  d3,d5
        add.w   d5,d5
        cmp.w   #5,d3                    ; more than 5?
        bhi.s   sdb_ipar                 ; ... yes
        moveq   #1,d1                    ; add parameter number to each
        move.l  a1,a0
sds_num
        move.b  d1,(a6,a0.l)
        addq.w  #2,a0                    ; next
        addq.w  #1,d1
        cmp.w   d3,d1
        ble.s   sds_num
*
        bra.s   sdb_smul                 ; send the parameters
*
* Set hotkey
*
sdp_key
        move.l  (a6),a1                 set null
        clr.w   (a6,a1.l)
        cmp.l   a3,a5                   any key?
        beq.s   sdb_stwo                ... no
*
        bsr.l   ut_gxnm1                get name
        bne.s   sdb_rts                 ... oops
        cmp.w   #1,(a6,a1.l)            one character string?
        bne.s   sdb_ipar
        addq.w  #1,a1
        clr.b   (a6,a1.l)               zero followed by key
        bra.s   sdb_stwo
*
* Set device name
*
sdp_dev
        bsr.l   ut_gxnm1                get name
        bne.s   sdb_rts                 ... oops
        move.w  (a6,a1.l),d5            set string length
        addq.w  #2,d5                   plus length
        move.b  #9,(a6,a1.l)            set short string
        bra.s   sdb_smul
*
        page
sdb_stwo
        moveq   #2,d5
sdb_smul
        moveq   #iob.smul,d7            send multiple
sdb_do
        move.l  a1,a5                   save parameter pointer
        lea     sdump_d(pc),a0
        moveq   #ioa.open,d0
        moveq   #-1,d1
        moveq   #ioa.kovr,d3
        trap    #2
        tst.l   d0
        bne.s   sdb_rts1
        move.l  a5,a1                   set pointer
        move.l  a4,a2                   and other pointer
        move.l  d7,d0                   action
        move.l  d5,d2                   and count / dump flag
        moveq   #-1,d3                  wait
        trap    #4
        trap    #3
        move.l  d0,d4                   save error
*
        moveq   #ioa.clos,d0            close dump channel
        trap    #2
*
        move.l  d4,d0
sdb_rts1
        rts
        page
*
sd_size equ     $1c
sd_org  equ     $18
sd_borwd equ    $20
swdef
        move.l  sd_size(a0),(a1)+       get size
        move.l  sd_org(a0),(a1)+
        move.w  sd_borwd(a0),d0
        sub.w   d0,-(a1)                adjust origin for border
        add.w   d0,d0
        sub.w   d0,-(a1)
        add.w   d0,-(a1)                adjust size
        add.w   d0,d0
        add.w   d0,-(a1)
        moveq   #0,d0
        rts
        end
