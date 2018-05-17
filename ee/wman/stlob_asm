; Window manager set loose item object  V0.00    1986   Tony Tebby   QJUMP

        section wman

        xdef    wm_stlob

        include dev8_keys_err
        include dev8_keys_wwork

;       d0  r   error return (out of range)
;       d1 c  p item number
;       a1 c  p pointer to object
;       a4 c  p working definition

reglist reg     d1/a3

wm_stlob
        movem.l reglist,-(sp)

;;;;;;;;;;;;;;;; wrong - but it is documented like this

        cmp.w   ww_nlitm(a4),d1          ; item number ok?
        bhs.s   wms_orng                 ; ... no
        mulu    #wwl.elen,d1
        move.l  ww_plitm(a4),a3          ; loose item list
        add.l   d1,a3
        move.l  a1,wwl_pobj(a3)          ; set pointer
        moveq   #0,d0
wms_exit
        movem.l (sp)+,reglist
        rts

wms_orng
        moveq   #err.orng,d0
        bra.s   wms_exit
        end
