; HOTKEY free routine  V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_thfre

        include 'dev8_keys_qlv'
        include 'dev8_ee_hk_data'
;+++
; Free routine for Hotkey Thing
;---
hk_thfre
        move.l  a0,-(sp)
        clr.b   hkd_act+hkd.thg(a1)      ; re-enable polling routine

        move.w  mem.rchp,a2              ; remove it
        jsr     (a2)
        move.l  (sp)+,a0
        rts
        end
