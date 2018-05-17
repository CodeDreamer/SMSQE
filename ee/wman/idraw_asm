* Draw information sub-window contents  V1.01     1986  Jonathan Oakley  QJUMP
*
        section wman
*
        xdef    wm_idraw
        xdef    wm_drinf
*
        xref    wm_trap3
        xref    wm_ssubw
*
        include dev8_keys_qdos_io
        include dev8_keys_wwork
        include dev8_keys_wstatus
*
*       d0  r   error return
*       d3 c  p information sub-windows to draw - bit clear to draw
*       a0  r   channel ID of window
*       a4 c  p pointer to working definition
*
*               all other registers preserved
*
reglist  reg    d1-d7/a1/a2/a3/a4
*
wm_idraw
        movem.l reglist,-(sp)           save registers
        move.l  ww_chid(a4),a0          get channel ID
        move.l  ww_xorg(a4),d7          and window origin
        bsr.s   wdr_info                draw some information sub-windows
wdr_exit
        movem.l (sp)+,reglist
        rts
        page
*
* do the information windows
*
wm_drinf
wdr_info
        move.l  ww_pinfo(a4),a3         pointer to list
        move.l  a3,d0                   is there one?
        beq.l   wdri_rts                ... no
wdr_inloop
        lsr.l   #1,d3                   draw this one?
        bcs.l   wdr_lpe                 no, see if there's another
*
        jsr     wm_ssubw(pc)            initialise sub window
        bne.l   wdri_rts                ... oops
*
        move.l  (a3)+,a2                point to object list
        move.l  a2,d0                   is there one?
        beq.l   wdr_innxt               ... no
wdr_iobj
        addq.l  #wwo_xorg,a2            get object origin
        move.l  (a2)+,d1
        move.b  (a2)+,d5                object type
        addq.l  #1,a2                   skip spare
        bgt.l   wdr_isprt
*
* text information object
*
        not.b   d5                      text, underscore position
        ext.w   d5
        move.l  d1,d4                   save origin

        move.w  d4,d2                   set cursor
        move.l  d4,d1
        swap    d1
        moveq   #iow.spix,d0
        jsr     wm_trap3(pc)

        move.w  (a2)+,d1                set ink colour
        moveq   #iow.sink,d0
        jsr     wm_trap3(pc)

        move.w  (a2)+,d6                standard size?
        bne.s   wdr_ssiz
        moveq   #-1,d6
wdr_ssiz
        move.w  d6,d1                   set character size
        lsr.w   #8,d1
        move.b  d6,d2
        moveq   #iow.ssiz,d0
        jsr     wm_trap3(pc)
        swap    d1
        muls    d1,d5                   underscore position
*
        move.l  (a2)+,d0                get string pointer
        beq.s   wdr_iodone
        move.l  d0,a1
        move.w  (a1)+,d2                string length
        moveq   #iob.smul,d0            and write it
        jsr     wm_trap3(pc)

        tst.w   d5                      underscore?
        bmi.s   wdr_spix                ... no

        moveq   #iow.spix,d0            reset position to remove pending nl
        move.w  d4,d2                   text, set cursor
        move.l  d4,d1
        swap    d1
        add.w   d5,d1                   right a bit
        addq.w  #1,d2                   down a bit
        jsr     wm_trap3(pc)

        moveq   #iow.sova,d0            overwrite
        moveq   #1,d1
        jsr     wm_trap3(pc)
        moveq   #iob.sbyt,d0
        moveq   #'_',d1
        jsr     wm_trap3(pc)            underscore first char
        moveq   #iow.sova,d0
        moveq   #0,d1                   and reset over

wdr_spix
        moveq   #iow.spix,d0            reset position to remove pending nl
        move.w  d4,d2                   text, set cursor
        move.l  d4,d1
        swap    d1
        jsr     wm_trap3(pc)

wdr_iodone
        tst.w   d6                      standard size?
        ble.s   wdr_ionxt               ... yes
        moveq   #iow.ssiz,d0            reset size
        moveq   #0,d1
        moveq   #0,d2
        jsr     wm_trap3(pc)
        bra.s   wdr_ionxt
*
* sprite information object
*
wdr_isprt
        add.w   #wwo_comb-wwo_ink,a2    skip ink colour
        subq.b  #4,d5                   is it sprite?
        bge.s   wdr_iblob               ... no, blob (or pattern)
        addq.l  #wwo_pobj-wwo_comb,a2   ... yes, skip combination
        moveq   #iop.wspt,d0
        move.l  (a2)+,a1                pointer to object
        bra.s   wdr_idrop               and drop information object
*
wdr_iblob
        move.l  (a2)+,a1                set pointer to blob
        move.l  (a2)+,d4                and to pattern
        tst.b   d5                      is pattern?
        bgt.s   wdr_ipatt               ... yes
        exg     a1,d4                   ... no, the other way round!
wdr_ipatt
        moveq   #iop.wblb,d0            write blob
wdr_idrop
        exg     a2,d4                   save next a2
        jsr     wm_trap3(pc)
        move.l  d4,a2                   restore information object list pointer
*
wdr_ionxt
        tst.w   (a2)                    end of information objects?
        bge.l   wdr_iobj                no
        bra.s   wdr_innxt               yes
*
wdr_lpe
        add.w   #wwi.elen,a3            advance to next information window
*
wdr_innxt
        tst.w   (a3)                    end of information windows?
        bge.l   wdr_inloop              ... no
        moveq   #0,d0                   ... yes
wdri_rts
        rts
        end
