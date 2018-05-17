; Find My JOB ID   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_myjb

        include 'dev8_keys_qdos_sms'
;+++
; Finds my JOB ID, preserving registers  
;
;       d0 c  r error code
;       d1 r    job ID
;       status returned according to D0
;---
gu_myjb
        movem.l d0/d2/a0,-(sp)
        moveq   #sms.info,d0             ; information
        trap    #do.sms2
        movem.l (sp)+,d0/d2/a0
        tst.l   d0
        rts
        end
