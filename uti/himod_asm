; Ensure machine works in highest resolution which is possible
; (Currently either 2 or 0 =4)

        section utility

        xdef    ut_himod

        include dev8_keys_qdos_sms
;+++
; Ensure current mode is highest resolution, otherwise set it to it
;
;               Entry                   Exit
;       d1.b                            mode
;---
ut_himod
        movem.l d2/a4,-(sp)        save a4
        moveq   #-1,d1
        moveq   #-1,d2
        moveq   #sms.dmod,d0
        trap    #do.sms2
        bclr    #3,d1           ensure current mode is 0 or 2
        beq.s   mode_ok
        moveq   #sms.dmod,d0
        trap    #do.sms2
mode_ok
        movem.l (sp)+,d2/a4
        rts

        end
