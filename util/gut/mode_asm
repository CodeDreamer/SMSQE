; Set mode       V2.00    1987 Tony Tebby   QJUMP

        section up

        xdef    gu_mode4                 ; set conditionally to mode 4

        include 'dev8_keys_qdos_sms'
;+++
; Set the mode to mode 4. If this actually requires a mode change, the
; mode 8 flag is set in the data block
;
;       d0  r   -1 no change otherwise old mode
;       status return unset
;---
gu_mode4
gmd.reg reg     d1/d2/d3/a4
        movem.l gmd.reg,-(sp)
        moveq   #-1,d1                   ; enquire
        bsr.s   gmd_do                   ; do mode call
        moveq   #-1,d0
        move.b  d1,d3                    ; right mode?
        beq.s   gmd_exit

        moveq   #0,d1                    ; set mode 0
        bsr.s   gmd_do
        move.b  d3,d0                    ; and old mode return

gmd_exit
        tst.l   d0
        movem.l (sp)+,gmd.reg
        rts

gmd_do
        moveq   #sms.dmod,d0             ; set / enquire mode
        moveq   #-1,d2
        trap    #do.sms2
        rts
        end
