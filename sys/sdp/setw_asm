* Setup sdp working data structure  V2.00     1987  Tony Tebby  QJUMP
*
        section sdp
*
        xdef    sdp_setw
*
        include dev8_keys_qdos_sms
        include dev8_keys_qdos_ioa
        include dev8_sys_sdp_data
*
        include dev8_mac_assert
*
*       call parameters
*
*       d0      mode (0 or 8)
*       d1      scale (0, 1 or 2)
*       d2      lsbyte true if right angle,  msb set if inverse
*       d3      lsbyte set if random
*       d4      x size
*       d5      y size
*       d6      start pixel (7 is lhs)
*       a0      channel ID
*       a1      pointer to window
*       a2      y address increment
*       a3      pointer to definition
*
*       preserved parameters
*
*       a5 c  p pointer to data block
*       a6
*
*       everything else is liable to be smashed
*
*
map.size equ    $08
inv.off  equ    $08
md8.off  equ    $10
rnd.off  equ    $20
px_mode4 dc.b   $0,$f,$1,$f,$4,$f,$5,$f
px_mode8 dc.b   $0,$1,$2,$3,$8,$9,$a,$b
px.dec4  equ    1
px.dec8  equ    2
*
sdp_setw
                         ; set channel and buffer to free a0
        assert  dp_chan,dp_supv-4,dp_bufpt-6 
        move.l  a0,dp_chan(a5)           ; set channel
        clr.l   dp_supv(a5)              ; clear supervisor flag and running ptr

                         ; set start addresses to free d6 and a1
        move.w  d6,dp_stpix(a5)          ; start pixel
        tst.b   d2                       ; right angle?
        beq.s   dst_strt                 ; ... no set start addresses
        move.w  d5,d6                    ; y pixels
        subq.w  #1,d6                    ; ypix-1
        move.w  a2,d7                    ; y increment
        mulu    d7,d6                    ; offset to start
        add.l   d6,a1                    ; start at bottom
dst_strt
        move.l  a1,dp_stadd(a5)          ; set start address

                         ; set up mode dependencies
        move.l  a3,a1                    ; pointer to map
        add.w   (a3)+,a1

        lea     px_mode4(pc),a0          ; assume mode 4 map
        sub.l   a4,a4                    ; pointer to controls
        moveq   #px.dec4,d6              ; bits/pixel
        moveq   #dp.mask4,d7             ; and mask
        tst.b   d0                       ; check mode
        beq.s   dst_map                  ; ... mode 4, set map
        addq.l  #px_mode8-px_mode4,a0    ; ... mode 8
        add.w   #md8.off,a1
        addq.l  #2,a4 
        lsr.w   #1,d4                    ; half as many pixels
        moveq   #px.dec8,d6
        moveq   #dp.mask8,d7
        bclr    #0,1+dp_stpix(a5)        ; even pixels only
dst_map
        move.w  d7,dp_mask(a5)           ; set mask value
        tst.l   d2                       ; inverse?
        bpl.s   dst_crnd                 ; ... no
        addq.w  #map.size,a1             ;
dst_crnd
        tst.b   d3                       ; random?
        sne     dp_randi(a5)             ; set flag
        beq.s   dst_smap                 ; ... no
        add.w   #rnd.off,a1              ; ... yes, move on to correct map

dst_smap
        moveq   #0,d3                    ; clear upper bytes
        moveq   #map.size-1,d0           ; set map
dst_smlp
        move.b  (a0)+,d3                 ; where to set
        move.b  (a1)+,dp_map(a5,d3.w)    ; set it
        dbra    d0,dst_smlp

                 ; get print method
        tst.b (a3)+ ********
;**        move.b  (a3)+,d3                 ; bit 1 horizontal, bit 2 escape
;**        btst    #dpc..hor,d3
        sne     dp_bitil(a5)
;**        btst    #dpc..esc,d3
;**        bne.s   dst_esc

                ; set initial output word
        move.b  (a3)+,d3                 ; add byte
        ext.w   d3
        ext.l   d3                       ; extended to long
        move.l  d3,dp_addwd(a5)
;**        clr.b   dp_escb(a5)              ; no escape byte
;**        bra.s   dst_dpbyt
;**
;**dst_esc
;**        move.b  (a3)+,dp_escb(a5)        ; escape byte
;**        clr.l   dp_addwd(a5)             ; no add word

                 ; number of dots per byte
dst_dpbyt 
        moveq   #0,d3
        move.b  (a3)+,d3                 ; get it
        move.w  d3,dp_nbwrd(a5)          ; and set it
        subq.b  #1,d3                    ; less one
        move.b  (a3)+,d0                 ; first bit
        move.b  d0,dp_bitst(a5)
        move.b  (a3)+,dp_bitin(a5)
        bgt.s   dst_bitn                 ; ... lsbit first
        neg.b   d3                       ; ... msb first
dst_bitn
        add.b   d3,d0
        move.b  d0,dp_bitnw(a5)          ; normal last bit
        move.b  (a3)+,dp_msbyt(a5)       ; set byte order

                 ; now set up the control and pattern definitions pointer
dst_ctrl
        add.l   a4,a3                    ; move to the right mode
        lsl.w   #3,d1                    ; index control table
        add.w   d1,a3
        lea     4(a3),a4
        add.w   (a3),a3                  ; real pointer to ctrl definition
        add.w   (a4),a4                  ; real pointer to pattern definition

        movem.w (a4)+,d2/d3              ; x/y pattern size

        assert  0,dpc_pwid,dpc_npas-2,dpc_hdpi-4
        move.w  (a3)+,d0                 ; print width (*10)
        move.w  (a3)+,dp_npass(a5)       ; number of passes
        mulu    (a3),d0                  ; max dots * 10
        divu    #10,d0 
        move.w  d0,dp_hdots(a5)
*
        addq.l  #dpc_cctl-dpc_hdpi,a3    ; move to colour control pointer
        move.l  a3,a1
        add.w   (a3)+,a1
        move.l  a3,dp_csequ(a5)          ; keep sequence pointer
        move.w  (a1)+,dp_ncolr(a5)       ; number of colour passes
        beq.s   dst_setp                 ; ... none
        lea     1(a1),a4                 ; pointer to colour pattern
dst_setp
        move.l  a4,dp_patt(a5)           ; save pointer
        page
                ; Now set up all the direction dependent values
        moveq   #0,d0                    ; check width
        move.w  dp_hdots(a5),d0
        divu    d2,d0                    ; max pixels wide
        sub.w   d4,d0                    ; too wide?
        bge.s   dst_sets                 ; no
        add.w   d0,d4                    ; make narrower
        asr.w   #1,d0                    ; two fewer pixels
        muls    d6,d0                    ; mode corrected
dst_adjx
        add.w   d0,dp_stpix(a5)          ; new start pixel
        bge.s   dst_sets                 ; ok
        addq.l  #2,dp_stadd(a5)          ; adjust address
        moveq   #8,d0                    ; adjust bit address
        bra.s   dst_adjx

dst_sets
        mulu    d2,d4                    ; width
        move.w  d4,dp_hdots(a5)
        mulu    d3,d5                    ; height
        move.w  d5,dp_vdots(a5)

        tst.b   dp_bitil(a5)             ; inline?
        bne.s   dst_ssil                 ; ... yes

        move.l  d5,d0                    ; height
        subq.l  #1,d0                    ; height-1
        divu    dp_nbwrd(a5),d0          ; (height-1) / nbwrd 
        addq.w  #1,d0                    ; +1 to include last (part?) row
        move.w  d0,dp_nrows(a5)          ; is number of rows
        move.w  d4,dp_wrdrw(a5)          ; wrdrw <- width 

        move.b  dp_bitnw(a5),dp_bitlw(a5); last bit in last word in row
        swap    d0                       ; (height-1) mod nbwrd
        tst.b   dp_bitin(a5)             ; * +-1
        bpl.s   dst_sbst
        neg.b   d0                       ; -1
dst_sbst
        add.b   dp_bitst(a5),d0          ; +start
        move.b  d0,dp_bitlr(a5)          ; last bit in last row
        move.b  d0,dp_bitlc(a5)          ; last bit in last corner
        bra.s   dst_spat

dst_ssil
        move.w  d5,dp_nrows(a5)          ; nrows <- height
        move.l  d4,d0                    ; width
        subq.l  #1,d0                    ; width-1
        divu    dp_nbwrd(a5),d0          ; (width-1) / nbwrd 
        addq.w  #1,d0                    ; +1 to include the last part row
        move.w  d0,dp_wrdrw(a5)          ; is number of words/row

        swap    d0                       ; (height-1) mod nbwrd
        tst.b   dp_bitin(a5)             ; * +-1
        bpl.s   dst_sbsi
        neg.b   d0                       ; -1
dst_sbsi
        add.b   dp_bitst(a5),d0          ; +start
        move.b  d0,dp_bitlw(a5)          ; last bit in last word in row
        move.b  dp_bitnw(a5),dp_bitlr(a5); last bit in last row
        move.b  d0,dp_bitlc(a5)          ; last bit

dst_spat
        assert  dp_ptrnm,dp_ptrin-2,dp_ptrsz-4,dp_ptwnm-6,dp_ptwin-8
        assert  dp_ptwin,dp_ptwsz-2,dp_ptbnm-4,dp_ptbin-6,dp_ptbsz-8
        lea     dp_ptrnm(a5),a0
        move.w  d2,d0
        mulu    d3,d0                    ; xpat*ypat
        moveq   #1,d1                    ; 1

        move.w  d3,(a0)+                 ; ptrnm <- ypat
        move.w  d2,(a0)+                 ; ptrin <- xpat
        move.w  d0,(a0)+                 ; ptrsz <- xpat*ypat
        tst.b   dp_bitil(a5)             ; inline?
        bne.s   dst_pinl                 ; ... yes
        move.w  d2,(a0)+                 ; ... no> ptwnm <- xpat
        move.w  d1,(a0)+                 ; ptwin <- 1
        move.w  d2,(a0)+                 ; ptwsz <- xpat
        move.w  d3,(a0)+                 ; ptbnm <- ypat
        move.w  d2,(a0)+                 ; ptbin <- xpat
        move.w  d0,(a0)+                 ; ptbsz <- xpat*ypat
        bra.s   dst_pclr

dst_pinl
        clr.w   (a0)+                    ; ptwnm <- 0
        clr.l   (a0)+                    ; ptwin <- 0
                                         ; ptwsz <- 0
        move.w  d2,(a0)+                 ; ptbnm <- xpat
        move.w  d1,(a0)+                 ; ptbin <- 1
        move.w  d2,(a0)+                 ; ptbsz <- xpat

dst_pclr
        tst.w   dp_ncolr(a5)             ; colour?
        beq.s   dst_spix                 ; ... no> set pixel
        clr.l   dp_ptrin(a5)             ; no increments/sizes
        clr.l   dp_ptwin(a5)
        clr.l   dp_ptbin(a5)

dst_spix
        assert  dp_pxrdc,dp_pxwdc-2,dp_pxbdc-4
        addq.l  #dp_pxrdc-dp_ptbsz-2,a0
        moveq   #8,d0
        move.w  d0,(a0)+                 ; pxrdc <- 8
        tst.b   dp_bitil(a5)             ; inline?
        bne.s   dst_spil                 ; yes
        move.w  d6,(a0)+                 ; pxwdc <- nbpix
        move.w  d0,(a0)+                 ; pxbdc <- 8

        assert  dp_rainc,dp_wainc-4,dp_bainc-8
        moveq   #2,d0
        add.w   #dp_rainc-dp_pxbdc-2,a0
        move.l  a2,(a0)+                 ; rainc <- yinc
        move.l  d0,(a0)+                 ; wainc <- 2
        move.l  a2,(a0)+                 ; bainc <- yinc
        
        move.w  dp_nbwrd(a5),d0
        mulu    dp_npass(a5),d0
        move.w  d0,dp_rbits(a5)          ; rbits <- nbwrd*npass
        bra.s   dst_ok

dst_spil
        move.w  d0,(a0)+                 ; pxwdc <- 8
        move.w  d6,(a0)+                 ; pxbdc <- nbpix

        assert  dp_rainc,dp_wainc-4,dp_bainc-8
        moveq   #2,d0
        add.w   #dp_rainc-dp_pxbdc-2,a0
        move.l  a2,(a0)+                 ; rainc <- yinc
        move.l  d0,(a0)+                 ; wainc <- 2
        move.l  d0,(a0)+                 ; bainc <- 2
        
        move.w  dp_npass(a5),dp_rbits(a5); rbits <- npass

dst_ok
        moveq   #0,d0                    ; can't fail
        rts
        end
