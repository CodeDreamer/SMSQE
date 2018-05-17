; Alt cursor PAN and SCROLL  V1.00   1986  Tony Tebby  QJUMP

        section wman

        xdef    wm_altps

        include dev8_keys_err
        include dev8_keys_wwork
        include dev8_keys_wstatus

;       d0  r   item number (or 0)
;       d1   s                       
;       d2 c  p upper cased alt cursor key
;       d3 c  p section number
;       a3 c  p pointer to sub-window definition

;               all other registers preserved

wm_altps
        moveq   #$18,d0                  ; get direction
        and.b   d2,d0
        lsr.b   #2,d0
        move.w  wap_itab(pc,d0.w),d0     ; set item!!
        move.b  d3,d0                    ; assume scroll

        btst    #wsi..pan,d0             ; is it pan?
        bne.s   wap_pan
        move.l  wwa_part+wwa.clen(a3),d1 ; pointer to scroll block
        bra.s   wap_look
wap_pan
        swap    d3
        move.b  d3,d0                    ; pan section
        swap    d3
        move.l  wwa_part(a3),d1          ; pointer to pan block
wap_look
        beq.s   wap_ok                   ; ... uncontrolled

        exg     d1,a2
        tst.w   (a2)                     ; any parts?
        exg     d1,a2
        bgt.s   wap_exit                 ; ... some

wap_ok
        moveq   #0,d0
wap_exit
        rts

wap_itab dc.b   wsi.lt,0,wsi.rt,0,wsi.up,0,wsi.dn,0
        end
