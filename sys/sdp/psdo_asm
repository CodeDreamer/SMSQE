; Set printer from sdd linkage and do  V2.00    1987  Tony Tebby   Qjump

        section sdp

        xdef    sdp_psdo

        xref    sdp_setw
        xref    sdp_dosv

        xref    dm_prt1
        xref    dm_prt2
        xref    dm_prt3
        xref    dm_prt4
        xref    dm_prt5
        xref    dm_prt6
        xref    dm_prt7
        xref    dm_prt8
        xref    dm_prt9
        xref    dm_prt10
        xref    dm_prt11
        xref    dm_prt12
        xref    dm_prt13
        xref    dm_prt14
        xref    dm_prt15
        xref    dm_prt16
        xref    dm_prt17
        xref    dm_prt18
        xref    dm_prt19
        xref    dm_prt20
;**        xref    dm_prt21
;**        xref    dm_prt22
;**        xref    dm_prt23
;**        xref    dm_prt24
;**        xref    dm_prt25

        include dev8_keys_sys
        include dev8_keys_con
        include dev8_keys_qdos_ioa
        include dev8_keys_qdos_pt
        include dev8_sys_sdp_ddlk 
        include dev8_sys_sdp_data
        include dev8_mac_assert

prtab
        dc.w    0
        dc.w    dm_prt1-prtab
        dc.w    dm_prt2-prtab
        dc.w    dm_prt20-prtab
        dc.w    dm_prt6-prtab
        dc.w    dm_prt10-prtab
        dc.w    dm_prt12-prtab
        dc.w    dm_prt11-prtab
        dc.w    dm_prt13-prtab
        dc.w    dm_prt8-prtab
        dc.w    dm_prt9-prtab
        dc.w    dm_prt3-prtab
        dc.w    dm_prt4-prtab
        dc.w    dm_prt5-prtab
        dc.w    dm_prt7-prtab
        dc.w    dm_prt15-prtab
        dc.w    dm_prt16-prtab
        dc.w    dm_prt17-prtab
        dc.w    dm_prt18-prtab
        dc.w    dm_prt19-prtab
        dc.w    dm_prt14-prtab
;**        dc.w    dm_prt21-prtab
;**        dc.w    dm_prt22-prtab
;**        dc.w    dm_prt23-prtab
;**        dc.w    dm_prt24-prtab
;**        dc.w    dm_prt25-prtab
prtmax  equ     20
        page

reglist  reg    d2/a0/a1/a2/a3
stk_aflg equ    $00
stk_wdef equ    $08
stk_base equ    $0c
stk_link equ    $10

sdp_psdo
        movem.l reglist,-(sp)

        lea     sdd_dev(a3),a0           ; open channel
        moveq   #ioa.open,d0
        moveq   #0,d1                    ; for zero
        moveq   #ioa.kovr,d3
        trap    #2
        tst.l   d0
        bne.l   sdpp_exit

        moveq   #8,d0
        and.b   sys_qlmr(a6),d0          ; display mode

        lea     sdd_prt(a3),a4           ; pointer to parameters
        lea     sdd_work(a3),a5          ; pointer to work area

        moveq   #0,d1
        move.b  (a4)+,d1                 ; printer definition
        beq.s   sdpp_pmin
        cmp.w   #prtmax,d1
        bls.s   sdpp_pset
sdpp_pmin
        moveq   #1,d1                    ; ... out of range
sdpp_pset
        lea     prtab(pc),a3             ; printer table
        add.w   d1,d1
        add.w   (a3,d1.w),a3             ; pointer to definition
*
        assert  sdd_scl,sdd_prt+1
        moveq   #0,d1
        move.b  (a4)+,d1                 ; scale
        ble.s   sdpp_smin
        subq.b  #1,d1
        cmp.b   #2,d1
        ble.s   sdpp_inv
sdpp_smin
        moveq   #0,d1

sdpp_inv
        assert  sdd_inv,sdd_scl+1
        moveq   #0,d2                    ; inverse / r/a
        tst.b   (a4)+                    ; inverse?
        beq.s   sdpp_rnd                 ; ... no
        bset    #31,d2

sdpp_rnd
        assert  sdd_rnd,sdd_inv+1
        moveq   #0,d3
        tst.b   (a4)+                    ; random?
        sne     d3

        assert  sdd_rta,sdd_rnd+1 
        tst.b   (a4)+                    ; right angle?
        sne     d2

        move.l  stk_base(sp),a2          ; base of save area
        move.l  stk_aflg(sp),d4          ; type of dump
        move.l  stk_wdef(sp),a4          ; window definition
        subq.b  #1,d4
        beq.s   sdpp_als                 ; all save area
        movem.w (a4),d4/d5/d6/d7         ; size,origin
        blt.s   sdpp_scr                 ; ... within screen
        bra.s   sdpp_ssv                 ; ... within save area
sdpp_als
        movem.w ptp_xsiz(a2),d4/d5       ; save area size
        moveq   #0,d6                    ; and origin
        moveq   #0,d7
sdpp_ssv
        move.b  ptp_mode(a2),d0          ; save area mode
        lea     ptp_bits(a2),a1          ; base of pixels
        move.w  ptp_rinc(a2),a2          ; row increment
        bra.s   sdpp_sadd

sdpp_scr
        move.l  stk_link(sp),a2
        lea     sdd_sinc(a2),a2
        move.l  (a2)+,a1                 ; base and increment
        move.w  (a2)+,-(sp)
        tst.w   d4                       ; any size?
        bne.s   sdpp_sinc                ; ... yes
        move.w  (a2)+,d4                 ; ... no, set whole screen
        move.w  (a2)+,d5
sdpp_sinc
        move.w  (sp)+,a2                 ; set increment

sdpp_sadd
        move.w  a2,-(sp)                 ; mulu a2,d7!!!
        mulu    (sp)+,d7                 ; row address
        add.l   d7,a1
        ror.l   #3,d6
        add.w   d6,d6
        add.w   d6,a1                    ; word address
        clr.w   d6
        rol.l   #3,d6                    ; pixel number
        not.b   d6
        addq.b  #8,d6                    ; pixel position

        and.b   #$8,d0                   ; 0 or 8 only!!

        jsr     sdp_setw                 ; set work area
        jsr     sdp_dosv                 ; do dump

sdpp_exit
        movem.l (sp)+,reglist
        rts

        end
