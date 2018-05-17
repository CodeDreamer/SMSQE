; Pan/scroll a menu sub-window  V1.02    1986  Tony Tebby   QJUMP

        section wman

        xdef    wm_pansc

        xref    wm_pan
        xref    wm_scrol

        include dev8_keys_qdos_pt


;       d0  r   error return
;       d2 c s  item number for pan/scroll
;       d3 cr   hit on pan/scroll bar - MSW hit position, LSW length
;       d4 cr   pt..pan or pt..scrl
;       a0 c  p channel ID
;       a3 c  p pointer to sub-window definition


wm_pansc
        cmp.b   #pt..scrl,d4             ; scroll?
        beq.l   wm_scrol                 ; ... yes
        cmp.b   #pt..pan,d4              ; pan?
        beq.l   wm_pan                   ; ... yes
        moveq   #0,d0
        rts
        end
