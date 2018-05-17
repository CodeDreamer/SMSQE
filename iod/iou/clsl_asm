; IO Utilities Clear Slave Blocks  V1.00    1988   Tony Tebby QJUMP

        section iou

        xdef    iou_clsl

        xref    iou_scsl

        include 'dev8_keys_sbt'
;+++
; IO Utilities clear slave blocks
;
;       d0 c    drive ID
;       all other registers preserved
;
;       error status 0
;---
iou_clsl
ics.reg  reg    a2
        move.l  a2,-(sp)
        lea     ics_clear,a2             ; clear out all our slave blocks
        jsr     iou_scsl

ics_exit
        move.l  (sp)+,a2
        rts

ics_clear
        move.b  #sbt.mpty,(a1)           ; free slave block
        moveq   #0,d0
        rts
        end
