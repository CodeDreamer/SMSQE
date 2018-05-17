; IO Utilities Truncate Slave Blocks  V1.00    1988   Tony Tebby QJUMP

        section iou

        xdef    iou_trsl

        xref    iou_scsl

        include 'dev8_keys_sbt'
;+++
; IO Utilities truncate slave blocks
;
;       d0 cr   drive ID, returned as last sector to keep
;       d5 c  p position to truncate to (-ve remove all)
;       d6 c  p (drive number) / file ID
;       all other registers preserved
;
;       error status < 0 all sectors thrown away
;---
iou_trsl
its.reg  reg    d1/a2
        movem.l its.reg,-(sp)
        move.l  d5,d1                    ; clear all?
        ble.s   its_clra                 ; yes, or keep first sector if empty
        subq.l  #1,d1
        lsr.l   #8,d1
        lsr.l   #1,d1
its_clra    
        lea     its_clrs,a2              ; clear out slave blocks beyond eof
        jsr     iou_scsl
        move.l  d1,d0
        movem.l (sp)+,its.reg
        rts

its_clrs
        cmp.w   sbt_file(a1),d6          ; our file?
        bne.s   its_ok                   ; ... no
        moveq   #0,d0
        move.w  sbt_blok(a1),d0          ; slave block off end?
        cmp.l   d1,d0
        ble.s   its_ok                   ; ... no
        move.b  #sbt.mpty,sbt_stat(a1)   ; ... yes, free it
its_ok
        moveq   #0,d0
        rts
        end
