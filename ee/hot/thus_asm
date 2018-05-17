; Procedure USE the HOTKEY  V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_thus

        xref    gu_thjmp
        xref    hk_name

        include 'dev8_keys_qdos_sms'
;+++
; Use hotkey thing: waits for up to 2.5 seconds
;
;       a3  r   address of hotkey thing
;       status returns standard
;---
hot_thus
reg.use reg     d1-d3/a0-a2
        movem.l reg.use,-(sp)
        moveq   #sms.uthg,d0             ; use thing
        moveq   #-1,d1
        moveq   #127,d3                  ; wait up to 2.5 seconds
        lea     hk_name,a0
        jsr     gu_thjmp
        move.l  a1,a3
        movem.l (sp)+,reg.use 
        rts
        end
