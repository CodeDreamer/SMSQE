; get window limits

        section utility

        include dev8_keys_qdos_io

        xdef    ut_gwlim
        xdef    ut_gwlma
        xdef    ut_gworg

        xref    gu_iow

;+++
; Get window origin.
;
;               Entry                   Exit
;       D2.l                            x-org.w | y-org.w
;       A0      channel ID              preserved
;
;       Error returns from IOP.FLIM
;---
ut_gworg
gwo.reg reg     d1/a1-a2
        movem.l gwo.reg,-(sp)
        subq.l  #8,sp           ; get room
        move.l  sp,a1           ; our workspace
        moveq   #0,d2
        moveq   #iop.flim,d0
        bsr     gu_iow           ; get window limits
        move.l  4(a1),d2
        addq.l  #8,sp           ; adjust stack
        movem.l (sp)+,gwo.reg
        tst.l   d0
        rts

;+++
; Get window limits using supplied channel ID.
;
;               Entry                   Exit
;       d1.l                            x-size.w | y-size.w
;       a0      channel ID              preserved
;
;       Error returns from IOP.FLIM
;---
ut_gwlma
gwl.reg reg     d2-d3/a0-a2
        movem.l gwl.reg,-(sp)
        bra.s   gwlm_all
;+++
; Get window limits. It returns the maximum screen size. If no Pointer Interface
; is present, the standard QL values are returned.
;
;               Entry                   Exit
;       d1.l                            x-size.w | y-size.w
;
;       Error returns from IOP.FLIM
;---
ut_gwlim
        movem.l gwl.reg,-(sp)
        sub.l   a0,a0           ; channel 0
gwlm_all
        subq.l  #8,sp           ; get room
        move.l  sp,a1           ; our workspace
        move.l  #$02000100,(a1) ; assume QL size, in case of error
        moveq   #0,d2
        moveq   #iop.flim,d0
        bsr     gu_iow           ; get window limits
        move.l  (a1),d1
        addq.l  #8,sp
        movem.l (sp)+,gwl.reg
        tst.l   d0
        rts

        end
