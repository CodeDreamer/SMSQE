* Hit areas of PAN and SCROLL sections V1.02    1986  Tony Tebby  QJUMP
*
        section wman
*
        xdef    wm_htpns
*
        include dev8_keys_err
        include dev8_keys_k
        include dev8_keys_wwork
        include dev8_keys_wstatus
*
*       d0  r   condition return (not found or item number)
*       d1 c  p pointer position
*       d2   sp window size
*       d3   r  window section
*       d4   r  hit size (internally hit max)
*       d5   r  hit position
*       a3 c  p pointer to sub-window definition
*
*               all other registers preserved
*
reglist reg d6/d7/a2
*
wm_htpns
        movem.l reglist,-(sp)
        moveq   #0,d3                    ; not in section
        move.l  wwa_xsiz(a3),d2          ; size
        move.l  d2,d4                    ; preset
        moveq   #0,d5                    ; origin
        move.l  wwa_part+wwa.clen(a3),d0 ; get y part control pointer
        beq.s   wht_htx                  ; ... uncontrolled, try x
        move.l  d0,a2
        move.l  #wsi.ctrl,d0             ; set scroll
        move.w  wwa_iatt+wwa_curw(a3),d7 ; item border
        add.w   d7,d7
        addq.w  #ww.scarr,d7             ; arrow size
        cmp.w   wwa_yoff(a3),d7          ; room for arrows?
        bgt.s   wht_htx
        bsr.s   wht_cksec
        bne.s   wht_done                 ; it's scroll, x is full width
wht_htx
        move.l  wwa_part(a3),d0          ; get x part control pointer
        beq.s   wht_done                 ; ... uncontrolled, done
        move.l  d0,a2
        moveq   #0,d0
        move.w  wwa_iatt+wwa_curw(a3),d7 ; item border
        lsl.w   #2,d7
        addq.w  #ww.pnarr,d7             ; arrow size
        cmp.w   wwa_xoff(a3),d7
        bgt.s   wht_done
        move.l  #wsi.ctrl+1<<wsi..pan,d0 ; set pan
        swap    d1                       ; swap to x
        swap    d2
        swap    d3
        swap    d4
        swap    d5
        bsr.s   wht_cksec
        swap    d1                       ; swap back
        swap    d2
        swap    d3
        swap    d4
        swap    d5
        tst.w   d0
        beq.s   wht_done
                                ; pan, reset y
        clr.w   d5                       ; y origin is 0
        move.w  d2,d4                    ; y max is window size

wht_done
        sub.l   d5,d4                    ; hit size
        movem.l (sp)+,reglist
        tst.w   d0
        rts


wht_cksec
        move.w  (a2)+,d6                 ; number
        subq.b  #1,d6
        blt.s   wht_ss0                  ; no sections, in section 0
        move.w  wss_spos(a2),d4          ; first section

wht_ssloop
        move.w  d4,d5                    ; next start
        add.w   d7,d4                    ; next limit
        cmp.w   d4,d1                    ; check up/left arrows
        blt.s   wht_supl                 ; ... found, set up/left
        move.w  d4,d5                    ; new start
        cmp.w   d3,d6                    ; last section?
        beq.s   wht_ssend                ; ... yes
        addq.w  #wss.ssln,a2             ; next section
        move.w  wss_spos(a2),d4
        sub.w   d7,d4                    ; end of this section
        cmp.w   d4,d1                    ; check if in section
        blt.s   wht_ssec                 ; set in section
        move.w  d4,d5                    ; new start
        add.w   d7,d4                    ; down/right arrows
        cmp.w   d4,d1
        blt.s   wht_sdnr                 ; ... set down/right
        addq.w  #1,d3
        bra.s   wht_ssloop
*
wht_ssend
        move.w  d2,d4                    ; bottom
        sub.w   d7,d4                    ; end of this section
        cmp.w   d4,d1                    ; check if in section
        blt.s   wht_ssec                 ; set in section
        move.w  d4,d5                    ; new start
        add.w   d7,d4                    ; down/right arrows
                                         ; must be there!!!

wht_sdnr
        bset    #wsi..dnr,d0             ; down / right
wht_supl
        move.b  d3,d0                    ; and part number
        tst.w   d0
        rts

wht_ss0
        move.w  d2,d4                    ; set whole window
wht_ssec
        moveq   #0,d0                    ; in section
        rts
        end
