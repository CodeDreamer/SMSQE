; Procedure FREE the HOTKEY  V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_thfr

        xref    gu_thjmp
        xref    hk_name

        include 'dev8_keys_qdos_sms'
;+++
; Free the hotkey thing
; 
;       status and D0 as call value of D0
;
;---
hot_thfr
reg.free reg    d0-d3/a0-a2
        movem.l reg.free,-(sp)
        moveq   #sms.fthg,d0             ; free thing
        moveq   #-1,d1                   ; mine !!!
        lea     hk_name,a0
        jsr     gu_thjmp
        movem.l (sp)+,reg.free
        tst.l   d0
        rts
        end
