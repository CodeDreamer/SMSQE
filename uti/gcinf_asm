; get con channel information           1991 Jochen Merz

        section utility

        xdef    ut_gcinf
;+++
; This routine retuns the screen base address and the line increment value.
;
;               Entry                   Exit
;       d1.w                            line increment
;       a0      window channel ID       preserved
;       a1                              screen base address
;---
ut_gcinf
        move.l  a2,-(sp)
        lea     get_spxo(pc),a2         ; EXTOP
        moveq   #9,d0                   ; ... yes, EXTOP
        moveq   #-1,d3                  ; wait in case screen is frozen
        trap    #3
        tst.l   d0                      ; just in case EXTOP does not exist
        move.l  (sp)+,a2
        rts

get_spxo
        move.w  $64(a0),d1              ; line increment
        move.l  $32(a0),a1              ; screen base
        moveq   #0,d0
        rts

        end
