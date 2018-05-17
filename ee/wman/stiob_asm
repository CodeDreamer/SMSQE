; Window manager set information object  V0.00    1986   Tony Tebby   QJUMP

        section wman

        xdef    wm_stiob

        include dev8_keys_err
        include dev8_keys_wwork

;       d0  r   error return (out of range)
;       d1 c  p window / item number
;       a1 c  p pointer to object
;       a4 c  p working definitiion

reglist reg     d1/a3

wm_stiob
        movem.l reglist,-(sp)
        move.l  d1,d0                    ; get window number
        swap    d0
        cmp.w   ww_ninfo(a4),d0          ; window number ok?
        bhs.s   wms_orng                 ; ... no
        mulu    #wwi.elen,d0

        move.l  ww_pinfo(a4),a3          ; information window list
        move.l  wwi_pobl(a3,d0.l),a3     ; object list

wms_oloop                        ; skip down object list
        tst.w   (a3)
        bmi.s   wms_orng                 ; off end of list
        add.w   #wwo.elen,a3             ; next entry
        dbra    d1,wms_oloop
                                 ; a3 is now pointing one beyond end

        move.l  a1,wwo_pobj-wwo.elen(a3)

        moveq   #0,d0
wms_exit
        movem.l (sp)+,reglist
        rts

wms_orng
        moveq   #err.orng,d0
        bra.s   wms_exit
        end
