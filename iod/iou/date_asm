; Find date   V2.00     1989  Tony Tebby   QJUMP

        section iou

        xdef    iou_date

        include 'dev8_keys_qdos_sms'

;+++
; Find date     
;
;       d1  r   (long) date
;
;       status return arbitrary
;---
iou_date
reg.date reg    d2/a0
        movem.l reg.date,-(sp)

        moveq   #sms.rrtc,d0
        trap    #do.sms2

        movem.l (sp)+,reg.date
        rts
        end
