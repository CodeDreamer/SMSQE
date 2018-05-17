; IO Utilities Change File ID of Slave Blocks  V1.00    1988   Tony Tebby QJUMP

        section iou

        xdef    iou_cisl

        xref    iou_scsl

        include 'dev8_keys_sbt'
;+++
; IO Utilities change ID in slave blocks
;
;       d0 cr   drive ID / ok
;       d1 c  p word, new file ID
;       d2 c  p word, old file ID
;       all other registers preserved
;
;       error status = 0
;---
iou_cisl
ici.reg  reg    a2
;       movem.l ici.reg,-(sp)
        move.l  a2,-(sp)
        lea     ici_flid,a2              ; change file ID
        jsr     iou_scsl
;       movem.l (sp)+,ici.reg
        move.l  (sp)+,a2
        rts

ici_flid
        cmp.w   sbt_file(a1),d2          ; our file?
        bne.s   ici_ok                   ; ... no
        move.w  d1,sbt_file(a1)          ; ... yes, set it
ici_ok
        moveq   #0,d0
        rts
        end
