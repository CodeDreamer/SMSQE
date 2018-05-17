; Functions to set HOTKEY    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_set
        xdef    hot_off

        xref    hot_thar
        xref    hot_rter
        xref    ut_gxnm1
        xref    ut_gtnm1

        xref    hk_set

        include 'dev8_keys_err'
        include 'dev8_ee_hk_vector'

hot_set
        moveq   #hks.on,d6               ; preset on action
        moveq   #0,d7                    ; and key
        jsr     ut_gtnm1                 ; first string
        bne.s   hs_rts
        addq.l  #8,a3                    ; next one
        cmp.l   a3,a5
        beq.s   hs_set                   ; ... there is not one

        cmp.w   #1,(a6,a1.l)             ; one character only?
        bne.s   hs_ipar
        moveq   #hks.rset,d6             ; reset action
        move.b  2(a6,a1.l),d7            ; keep character
        bra.s   hs_gstr                  ; and get second string / name

hot_off
        moveq   #hks.off,d6              ; preset off action
hs_gstr
        jsr     ut_gxnm1                 ; get just one string
        bne.s   hs_rts
hs_set
        lea     hk_set,a2                ; set hotkey
        move.l  d6,d0                    ; action
        move.l  d7,d1                    ; key (if required)
        jsr     hot_thar                 ; action a1 relative a6
        jmp     hot_rter

hs_ipar
        moveq   #err.ipar,d0
hs_rts
        rts
        end
